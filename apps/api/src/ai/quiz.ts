import { z } from "zod";
import { parseSlides, type Slide } from "./slides.js";

/**
 * AI adapter for the quiz generator (DraftQuiz, RegenerateQuiz). One function, always returns valid content:
 * with ANTHROPIC_API_KEY it asks the model; on ANY failure (no key, timeout, non-200, invalid JSON) it falls
 * back to a deterministic question built from the slide text, so a demo never depends on the network.
 * Never log the key.
 */

export type GeneratedQuestion = { question: string; options: { label: string; text: string }[]; correctLabel: string };
export type QuizGeneratorInput = { sourceText: string; slideNumber: number; avoidQuestion?: string };

const LABELS = ["A", "B", "C", "D"] as const;
const API_URL = "https://api.anthropic.com/v1/messages";
const TIMEOUT_MS = 15_000;
const MAX_TOKENS = 700;

export async function generateQuizQuestion(input: QuizGeneratorInput): Promise<GeneratedQuestion> {
  const key = process.env.ANTHROPIC_API_KEY;
  if (key) {
    try {
      return await askModel(key, input);
    } catch (e) {
      console.warn(`quiz generator: deterministic fallback (${e instanceof Error ? e.name : "error"})`);
    }
  }
  return deterministicQuestion(input);
}

// --- model path ----------------------------------------------------------------------------------------------

const ModelAnswer = z.object({
  question: z.string().trim().min(5).max(300),
  options: z.array(z.string().trim().min(1).max(200)).length(4).refine((o) => new Set(o).size === 4, "options must differ"),
  correctIndex: z.number().int().min(0).max(3),
});

async function askModel(key: string, input: QuizGeneratorInput): Promise<GeneratedQuestion> {
  const slide = pickSlide(parseSlides(input.sourceText), input.slideNumber);
  const where = slide.number > 0 ? `la diapositiva ${slide.number}` : "el material de la clase";
  const avoid = input.avoidQuestion ? `\nNo repitas esta pregunta: «${input.avoidQuestion}».` : "";
  const user = [
    `Texto de ${where}:`,
    `"""\n${slide.text}\n"""`,
    "",
    `Escribe UNA pregunta de opción múltiple sobre este texto, con exactamente 4 opciones: exactamente una correcta y tres plausibles pero incorrectas.${avoid}`,
    `Responde SOLO con JSON, con esta forma: {"question": "...", "options": ["...", "...", "...", "..."], "correctIndex": 0}`,
    "donde correctIndex es la posición (0 a 3) de la opción correcta.",
  ].join("\n");

  const res = await fetch(API_URL, {
    method: "POST",
    headers: { "x-api-key": key, "anthropic-version": "2023-06-01", "content-type": "application/json" },
    body: JSON.stringify({
      model: process.env.ANTHROPIC_MODEL || "claude-haiku-5-5",
      max_tokens: MAX_TOKENS,
      system: "Eres docente de educación superior. Escribes preguntas de opción múltiple para verificar la comprensión de una clase. Respondes solo con JSON válido.",
      messages: [{ role: "user", content: user }],
    }),
    signal: AbortSignal.timeout(TIMEOUT_MS),
  });
  if (!res.ok) throw new Error(`HTTP ${res.status}`);

  const body = (await res.json()) as { content?: { type: string; text?: string }[] };
  const text = (body.content ?? []).filter((b) => b.type === "text").map((b) => b.text ?? "").join("");
  const answer = ModelAnswer.parse(JSON.parse(extractJson(text)));

  const order = shuffle(answer.options.map((_, i) => i), Math.random);
  return {
    question: answer.question,
    options: order.map((original, pos) => ({ label: LABELS[pos], text: answer.options[original] })),
    correctLabel: LABELS[order.indexOf(answer.correctIndex)],
  };
}

/** The model may wrap the JSON in a code fence or add a sentence around it: take the outermost object. */
function extractJson(text: string): string {
  const start = text.indexOf("{");
  const end = text.lastIndexOf("}");
  if (start < 0 || end <= start) throw new Error("no JSON object in the answer");
  return text.slice(start, end + 1);
}

// --- deterministic fallback (no network) ---------------------------------------------------------------------

/**
 * Correct option: «Según la diapositiva N: <first sentence of the slide>». Distractors: the same form with the first
 * sentences of the OTHER slides, then simple negations of the correct one. The order is seeded from the question
 * (and the question being avoided), so a regeneration reshuffles. Never throws.
 */
function deterministicQuestion(input: QuizGeneratorInput): GeneratedQuestion {
  try {
    const slides = parseSlides(input.sourceText);
    const target = pickSlide(slides, input.slideNumber);
    const n = target.number;
    const prefix = (num: number) => (num > 0 ? `Según la diapositiva ${num}: ` : "Según el material de la clase: ");
    const sentence = firstSentence(target.text) || "el contenido de la clase";
    const correct = `${prefix(n)}${sentence}`;
    const question = n > 0 ? `¿Cuál de estas afirmaciones corresponde a la diapositiva ${n}?` : "¿Cuál de estas afirmaciones corresponde al material de la clase?";

    const others = slides.filter((s) => s !== target).map((s) => `${prefix(s.number)}${firstSentence(s.text)}`);
    const lower = lowerFirst(sentence);
    const negations = [`${prefix(n)}no es cierto que ${lower}`, `${prefix(n)}no aparece en el texto`, `${prefix(n)}lo opuesto a lo que se explica`];
    const distractors = distinct([...others, ...negations].filter((t) => t !== correct)).slice(0, 3);

    const seed = hash(`${question}|${input.avoidQuestion ?? ""}|${n}`);
    const options = shuffle([correct, ...distractors], mulberry32(seed));
    return {
      question,
      options: options.map((text, i) => ({ label: LABELS[i], text })),
      correctLabel: LABELS[options.indexOf(correct)],
    };
  } catch {
    return LAST_RESORT;
  }
}

const LAST_RESORT: GeneratedQuestion = {
  question: "¿Cuál de estas afirmaciones corresponde al tema de la clase?",
  options: [
    { label: "A", text: "El tema de la clase" },
    { label: "B", text: "Un tema que no aparece en la clase" },
    { label: "C", text: "Un procedimiento distinto" },
    { label: "D", text: "Ninguna de las anteriores" },
  ],
  correctLabel: "A",
};

// --- helpers -------------------------------------------------------------------------------------------------

/** The requested slide if it exists, else the first slide of the text (0 = the unnumbered text). */
function pickSlide(slides: Slide[], wanted: number): Slide {
  return slides.find((s) => s.number === wanted) ?? slides[0];
}

function firstSentence(text: string): string {
  const flat = text.replace(/\s+/g, " ").trim();
  const sentence = flat.split(/(?<=[.!?])\s/)[0] ?? flat;
  return sentence.length > 200 ? `${sentence.slice(0, 197).trimEnd()}…` : sentence;
}

const lowerFirst = (s: string) => (s ? s[0].toLowerCase() + s.slice(1) : s);
const distinct = <T>(items: T[]) => [...new Set(items)];

function shuffle<T>(items: T[], rng: () => number): T[] {
  const out = [...items];
  for (let i = out.length - 1; i > 0; i--) {
    const j = Math.floor(rng() * (i + 1));
    [out[i], out[j]] = [out[j], out[i]];
  }
  return out;
}

/** FNV-1a: a stable seed from a string. */
function hash(s: string): number {
  let h = 0x811c9dc5;
  for (let i = 0; i < s.length; i++) h = Math.imul(h ^ s.charCodeAt(i), 0x01000193);
  return h >>> 0;
}

/** mulberry32: a small seeded generator, so the fallback is reproducible for the same input. */
function mulberry32(seed: number): () => number {
  let a = seed;
  return () => {
    a = (a + 0x6d2b79f5) | 0;
    let t = Math.imul(a ^ (a >>> 15), 1 | a);
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t;
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
  };
}
