import { cx } from "./cx";
import { Heading, type TitleSize } from "./Typography";

export function formatClock(totalSeconds: number): string {
  const s = Math.max(0, Math.floor(totalSeconds));
  return `${Math.floor(s / 60)}:${String(s % 60).padStart(2, "0")}`;
}

/** m:ss display in Nunito 900. 200 for the questions round, 48 in the quiz header, 40 in "Responde ahora". */
export function Countdown({ seconds, size = 48, className }: { seconds: number; size?: Extract<TitleSize, 200 | 48 | 40>; className?: string }) {
  return (
    <Heading as="span" size={size} className={cx(size === 200 && "leading-[0.9]", className)}>
      <span role="timer" aria-label={`${formatClock(seconds)} restantes`}>{formatClock(seconds)}</span>
    </Heading>
  );
}
