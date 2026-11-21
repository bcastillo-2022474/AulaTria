import type { ReactNode } from "react";
import { quizzes } from "@aula/rpc";
import { AppBar, Banner, Button, Content, OPTION_COLORS, OptionRow, Screen, Text } from "../../../design-system";
import { SignOutButton } from "../../auth/SignOutButton";
import { errorMessage } from "../../../rpc/hooks";

/** Frame shared by the quiz screens: the AppBar ("CURSO · CLASE n" left, state right) and the stage content area. */
export function QuizShell({ courseLabel, status, children }: { courseLabel?: string; status: string; children: ReactNode }) {
  return (
    <Screen>
      <AppBar left={courseLabel} right={<><span>{status}</span><SignOutButton /></>} />
      <Content>{children}</Content>
    </Screen>
  );
}

/** Loading / error (with retry) body for a failed or pending query. */
export function QuizLoading({ error, onRetry, label }: { error: unknown; onRetry: () => void; label: string }) {
  if (!error) return <Text as="p" size={16}>{label}</Text>;
  return (
    <div className="flex flex-col items-start gap-4">
      <Banner tone="danger">{errorMessage(error)}</Banner>
      <Button variant="ghost" size="48" onClick={onRetry}>Reintentar</Button>
    </div>
  );
}

export const QUIZ_LABELS = ["A", "B", "C", "D"] as const;

/**
 * The four options A to D (A red, B blue, C yellow, D green: fixed by the design).
 * draft: the correct one carries the check. live: vote bars. closed: the correct one bright, the rest dimmed.
 */
export function QuizOptions({ quiz, mode }: { quiz: quizzes.Quiz; mode: "draft" | "live" | "closed" }) {
  const counts = quiz.countsByLabel;
  const max = Math.max(0, ...QUIZ_LABELS.map((l) => counts[l] ?? 0));
  return (
    <div className="flex flex-col gap-2.5">
      {quiz.options.map((option, i) => {
        const count = counts[option.label] ?? 0;
        return (
          <OptionRow
            key={option.label}
            letter={option.label}
            color={OPTION_COLORS[i] ?? "danger"}
            text={option.text}
            height={mode === "closed" ? 56 : 60}
            correct={mode === "draft" && option.label === quiz.correctLabel}
            count={mode === "draft" ? undefined : count}
            share={mode === "draft" ? undefined : max > 0 ? count / max : 0}
            dimmed={mode === "closed" && option.label !== quiz.correctLabel}
          />
        );
      })}
    </div>
  );
}
