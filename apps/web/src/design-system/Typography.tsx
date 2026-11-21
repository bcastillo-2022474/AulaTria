import type { ReactNode } from "react";
import { cx } from "./cx";

export type TitleSize = 200 | 130 | 110 | 88 | 72 | 64 | 58 | 56 | 52 | 48 | 46 | 44 | 40 | 38 | 36 | 34 | 32 | 30 | 28 | 26 | 24 | 22 | 20;

// Literal class names so Tailwind can see them.
const titleClass: Record<TitleSize, string> = {
  200: "text-title-200", 130: "text-title-130", 110: "text-title-110", 88: "text-title-88", 72: "text-title-72",
  64: "text-title-64", 58: "text-title-58", 56: "text-title-56", 52: "text-title-52", 48: "text-title-48",
  46: "text-title-46", 44: "text-title-44", 40: "text-title-40", 38: "text-title-38", 36: "text-title-36",
  34: "text-title-34", 32: "text-title-32", 30: "text-title-30", 28: "text-title-28", 26: "text-title-26",
  24: "text-title-24", 22: "text-title-22", 20: "text-title-20",
};

/** Nunito 900 heading. Dominant page title is 48 (list pages) / 44 (live, forms). */
export function Heading({ as: Tag = "h1", size = 48, className, children }: {
  as?: "h1" | "h2" | "h3" | "span"; size?: TitleSize; className?: string; children: ReactNode;
}) {
  return <Tag className={cx("type-title m-0", titleClass[size], className)}>{children}</Tag>;
}

/** 13px small label in stage-ink with .04em tracking ("CLASES ANTERIORES"). */
export function Eyebrow({ className, children }: { className?: string; children: ReactNode }) {
  return <span className={cx("type-eyebrow", className)}>{children}</span>;
}

/** Body copy in stage-ink. */
export function Text({ size = 15, as: Tag = "span", tone = "soft", className, children }: {
  size?: 14 | 15 | 16 | 17 | 18; as?: "span" | "p"; tone?: "soft" | "strong"; className?: string; children: ReactNode;
}) {
  const s = { 14: "text-body-14", 15: "text-body-15", 16: "text-body-16", 17: "text-body-17 leading-normal", 18: "text-body-18 leading-normal" }[size];
  return <Tag className={cx("m-0", s, tone === "soft" ? "text-stage-ink" : "text-on-stage", className)}>{children}</Tag>;
}
