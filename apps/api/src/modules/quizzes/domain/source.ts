import type { Slide } from "../../../ai/slides.js";

/** A topic the students doubted in this lesson, with how many students wrote into it (doubt_group + count of doubt). */
export type DoubtGroupRef = { title: string; slideNumber: number; students: number };

/**
 * Which slide the question is about. An explicit slide wins when the material has it. Otherwise the slide with the
 * most doubts (summed student counts of its groups; ties go to the lower slide). Without doubts: the first slide.
 * Returns 0 for a text without slide markers. Pure: no I/O.
 */
export function chooseSlide(slides: Slide[], requested: number, groups: DoubtGroupRef[]): number {
  if (requested > 0 && slides.some((s) => s.number === requested)) return requested;
  const bySlide = new Map<number, number>();
  for (const g of groups) {
    if (slides.some((s) => s.number === g.slideNumber)) bySlide.set(g.slideNumber, (bySlide.get(g.slideNumber) ?? 0) + g.students);
  }
  let best: { slide: number; students: number } | null = null;
  for (const [slide, students] of bySlide) {
    if (!best || students > best.students || (students === best.students && slide < best.slide)) best = { slide, students };
  }
  return best ? best.slide : slides[0].number;
}

/**
 * "Por que esta pregunta": the most doubted topic of the slide, when there is one. Null otherwise. Pure.
 *   "14 estudiantes dejaron dudas sobre por que un indice puede hacer mas lentas las escrituras."
 */
export function doubtRationale(groups: DoubtGroupRef[], slide: number): string | null {
  const top = groups
    .filter((g) => g.slideNumber === slide && g.students > 0)
    .reduce<DoubtGroupRef | null>((best, g) => (!best || g.students > best.students ? g : best), null);
  if (!top) return null;
  const topic = top.title.replace(/[¿?]/g, "").trim().toLowerCase();
  return top.students === 1
    ? `1 estudiante dejó dudas sobre ${topic}.`
    : `${top.students} estudiantes dejaron dudas sobre ${topic}.`;
}
