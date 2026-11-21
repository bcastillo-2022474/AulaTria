import { quizzes } from "@aula/rpc";
import { Button, Card, Eyebrow, Heading, Text } from "../../../design-system";
import { QUIZ_LABELS, QuizOptions, QuizShell } from "./QuizShell";

/** Docente-C-QuizResultados: correct option bright, the rest dimmed, the percent and the insight. */
export function QuizResults({ quiz, courseLabel }: { quiz: quizzes.Quiz; courseLabel?: string }) {
  const total = QUIZ_LABELS.reduce((sum, l) => sum + (quiz.countsByLabel[l] ?? 0), 0);
  const right = quiz.countsByLabel[quiz.correctLabel] ?? 0;

  return (
    <QuizShell courseLabel={courseLabel} status="Quiz · resultado">
      <div className="flex min-h-0 flex-1 flex-col gap-5">
        <div className="flex flex-col gap-2.5">
          <Eyebrow>Resultado</Eyebrow>
          <Heading as="h2" size={34}>{quiz.question}</Heading>
        </div>
        <QuizOptions quiz={quiz} mode="closed" />
        <Card padding={22} className="flex items-center gap-7">
          <Heading as="span" size={48}>{quiz.correctPercent}%</Heading>
          <Text as="p" size={15} className="leading-normal">
            {`acertó (${right} de ${total}).`}
            {quiz.insight ? ` ${quiz.insight}` : ""}
          </Text>
        </Card>
        <div className="mt-auto flex items-start justify-end gap-3">
          <div className="flex flex-col items-center gap-1">
            <Button variant="ghost" size="56" disabled>Repasar el concepto</Button>
            <Text size={14}>Próximamente</Text>
          </div>
          <Button size="56" href={`/lessons/${quiz.lessonId}/live`}>Continuar clase</Button>
        </div>
      </div>
    </QuizShell>
  );
}
