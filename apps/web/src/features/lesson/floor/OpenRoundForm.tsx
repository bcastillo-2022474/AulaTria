import { useState, type FormEvent } from "react";
import { Banner, Button, Field, Heading, Text } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useOpenFloorRound } from "../../../rpc/gen/hooks";

/** Asks the question that opens a floor round ("Toma de la palabra"). Shown when no round is open. */
export function OpenRoundForm({ lessonId, onOpened }: { lessonId: string; onOpened: () => void }) {
  const [prompt, setPrompt] = useState("");
  const open = useOpenFloorRound({ onSuccess: onOpened });
  const text = prompt.trim();

  function submit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    if (text.length === 0 || open.isPending) return;
    open.mutate({ lessonId, prompt: text });
  }

  return (
    <form onSubmit={submit} className="flex w-[640px] max-w-full flex-col gap-6">
      <div className="flex flex-col gap-2">
        <Heading size={44}>Abrir ronda de palabra</Heading>
        <Text as="p" size={16}>Los estudiantes levantan la mano para responder, en el orden en que la levantan.</Text>
      </div>
      <Field
        label="Pregunta" required maxLength={300} hint="Máximo 300 caracteres."
        value={prompt} onChange={(e) => setPrompt(e.target.value)} autoFocus
      />
      {open.error ? <Banner tone="danger">{errorMessage(open.error)}</Banner> : null}
      <Button type="submit" size="60" block disabled={text.length === 0 || open.isPending}>
        {open.isPending ? "Abriendo…" : "Abrir ronda"}
      </Button>
    </form>
  );
}
