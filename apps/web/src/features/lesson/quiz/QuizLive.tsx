import type { Timestamp } from "@bufbuild/protobuf/wkt";
import { quizzes } from "@aula/rpc";
import { Banner, Button, Countdown, Eyebrow, Heading, Progress, Text } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useCloseQuiz } from "../../../rpc/gen/hooks";
import { useCountdown } from "../../../rpc/time";
import { QuizOptions, QuizShell } from "./QuizShell";

/** Docente-C-QuizVivo: countdown, "N de M", live vote bars, "Mostrar resultado". */
export function QuizLive({ quiz, serverTime, courseLabel }: { quiz: quizzes.Quiz; serverTime?: Timestamp; courseLabel?: string }) {
  const close = useCloseQuiz();
  const left = useCountdown(quiz.closesAt, serverTime);
  const percent = quiz.timeLimitSeconds > 0 ? Math.min(100, (left / quiz.timeLimitSeconds) * 100) : 0;

  return (
    <QuizShell courseLabel={courseLabel} status="Quiz en vivo">
      <div className="flex min-h-0 flex-1 flex-col gap-[22px]">
        <div className="flex items-end justify-between gap-10">
          <div className="flex flex-1 flex-col gap-2.5">
            <Eyebrow>Quiz · en vivo</Eyebrow>
            <Heading as="h2" size={36}>{quiz.question}</Heading>
          </div>
          <div className="flex w-[260px] shrink-0 flex-col gap-2.5">
            <div className="flex items-baseline justify-between">
              <Countdown seconds={left} />
              <Text size={14}>{quiz.answeredCount} de {quiz.enrolledCount}</Text>
            </div>
            <Progress value={percent} label="Tiempo restante" />
          </div>
        </div>
        <QuizOptions quiz={quiz} mode="live" />
        <div className="mt-auto flex flex-col items-end gap-3">
          {close.error ? <Banner tone="danger">{errorMessage(close.error)}</Banner> : null}
          <Button size="56" disabled={close.isPending} onClick={() => close.mutate({ quizId: quiz.id })}>
            {close.isPending ? "Cerrando…" : "Mostrar resultado"}
          </Button>
        </div>
      </div>
    </QuizShell>
  );
}
