import { Button, Card, Eyebrow, Heading, Text } from "../../../design-system";

/** The round is over: the outcome, then back to the class or a new question. */
export function RoundClosed({ lessonId, correct, onNewQuestion }: {
  lessonId: string; correct: boolean; onNewQuestion: () => void;
}) {
  return (
    <div className="flex w-[640px] max-w-full flex-col gap-6">
      <Card padding={28} className="flex flex-col gap-[18px]">
        <Eyebrow>Ronda cerrada</Eyebrow>
        <Heading size={56}>{correct ? "Respuesta correcta" : "Ronda cerrada"}</Heading>
        <Text as="p" size={16}>
          {correct ? "Alguien respondió bien y la ronda se cerró." : "La ronda se cerró sin una respuesta correcta."}
        </Text>
        <div className="flex gap-3">
          <Button href={`/lessons/${lessonId}/live`} size="56" block>Volver a la clase</Button>
          <Button variant="ghost" size="56" block onClick={onNewQuestion}>Nueva pregunta</Button>
        </div>
      </Card>
    </div>
  );
}
