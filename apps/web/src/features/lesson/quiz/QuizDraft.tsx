import { quizzes } from "@aula/rpc";
import { Banner, Button, Card, Eyebrow, Heading, Text } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useLaunchQuiz, useRegenerateQuiz, useUpdateQuizTime } from "../../../rpc/gen/hooks";
import { QuizOptions, QuizShell } from "./QuizShell";

const TIME_CHOICES = [20, 30, 45, 60, 90] as const;

/** Docente-C-QuizArmar: review the drafted question, set the time, launch or regenerate. */
export function QuizDraft({ quiz, courseLabel }: { quiz: quizzes.Quiz; courseLabel?: string }) {
  const regenerate = useRegenerateQuiz();
  const updateTime = useUpdateQuizTime();
  const launch = useLaunchQuiz();
  const busy = regenerate.isPending || updateTime.isPending || launch.isPending;
  const error = regenerate.error ?? updateTime.error ?? launch.error;

  return (
    <QuizShell courseLabel={courseLabel} status="Quiz · borrador">
      <div className="flex min-h-0 flex-1 gap-10">
        <div className="flex min-w-0 flex-1 flex-col gap-[22px]">
          <Eyebrow>{quiz.slideNumber > 0 ? `Borrador · diap. ${quiz.slideNumber}` : "Borrador"}</Eyebrow>
          <Heading as="h2" size={36}>{quiz.question}</Heading>
          <QuizOptions quiz={quiz} mode="draft" />
        </div>
        <div className="flex w-[360px] shrink-0 flex-col gap-4">
          {quiz.rationale ? (
            <Card padding={24} className="flex flex-col gap-2">
              <Eyebrow>Por qué esta pregunta</Eyebrow>
              <Text as="p" size={15} className="leading-[1.45]">{quiz.rationale}</Text>
            </Card>
          ) : null}
          <Card padding={24} className="flex flex-col gap-3">
            <div className="flex items-center justify-between">
              <Text size={15} tone="strong">Tiempo por pregunta</Text>
              <Heading as="span" size={24}>{quiz.timeLimitSeconds} s</Heading>
            </div>
            <div className="flex gap-1.5" role="group" aria-label="Tiempo por pregunta">
              {TIME_CHOICES.map((seconds) => (
                <Button
                  key={seconds}
                  size="48"
                  variant={seconds === quiz.timeLimitSeconds ? "accent" : "ghost"}
                  className="!px-0 flex-1"
                  aria-pressed={seconds === quiz.timeLimitSeconds}
                  disabled={busy}
                  onClick={() => updateTime.mutate({ quizId: quiz.id, timeLimitSeconds: seconds })}
                >
                  {seconds} s
                </Button>
              ))}
            </div>
          </Card>
          <div className="mt-auto flex flex-col gap-3">
            {error ? <Banner tone="danger">{errorMessage(error)}</Banner> : null}
            <Button size="56" block disabled={busy} onClick={() => launch.mutate({ quizId: quiz.id })}>
              {launch.isPending ? "Lanzando…" : `Lanzar quiz · ${quiz.timeLimitSeconds} s`}
            </Button>
            <Button size="52" variant="ghost" block disabled={busy} onClick={() => regenerate.mutate({ quizId: quiz.id })}>
              {regenerate.isPending ? "Armando otra pregunta…" : "Regenerar pregunta"}
            </Button>
          </div>
        </div>
      </div>
    </QuizShell>
  );
}
