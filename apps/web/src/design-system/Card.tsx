import type { HTMLAttributes } from "react";
import { cx } from "./cx";

export type CardPadding = 0 | 18 | 20 | 22 | 24 | 26 | 28 | 40;
const pad: Record<CardPadding, string> = { 0: "p-0", 18: "p-[18px]", 20: "p-5", 22: "p-[22px]", 24: "p-6", 26: "p-[26px]", 28: "p-7", 40: "p-10" };

/** stage: translucent card with 6px lift shadow (all teacher/admin cards). paper: white card (student parity). */
export function Card({ tone = "stage", padding = 28, className, ...rest }: HTMLAttributes<HTMLDivElement> & { tone?: "stage" | "paper"; padding?: CardPadding }) {
  return (
    <div {...rest} className={cx(
      "box-border rounded-card",
      tone === "stage" ? "bg-stage-card text-on-stage shadow-lift" : "bg-surface text-ink shadow-soft",
      pad[padding], className,
    )} />
  );
}
