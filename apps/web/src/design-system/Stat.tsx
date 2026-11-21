import { cx } from "./cx";
import { Heading, type TitleSize } from "./Typography";

/** Big number with caption, baseline-aligned: "84%  voy bien", "27  de 38 presentes". Number line-height 1 (.9 at 110+). */
export function Stat({ value, caption, size = 64, captionSize = 20, accent, className }: {
  value: string; caption?: string; size?: TitleSize; captionSize?: 20 | 22 | 24; accent?: boolean; className?: string;
}) {
  const cap = { 20: "text-body-20", 22: "text-body-22", 24: "text-body-24" }[captionSize];
  return (
    <div className={cx("flex items-baseline gap-3", className)}>
      <Heading as="span" size={size} className={cx(size >= 110 ? "leading-[0.9]" : "leading-none", accent && "text-accent")}>{value}</Heading>
      {caption ? <span className={cx(cap, "text-stage-ink")}>{caption}</span> : null}
    </div>
  );
}
