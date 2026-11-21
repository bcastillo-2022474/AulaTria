import type { ReactNode } from "react";
import { type BrandColor, brandBg, cx } from "./cx";

export type BadgeTone = "ok" | "accent" | "chart";

const tone: Record<BadgeTone, string> = {
  ok: "bg-ok text-ok-ink",             // "Activo"
  accent: "bg-accent text-ink-deep",   // "Invitación pendiente"
  chart: "bg-stage-card text-on-stage", // "Quiz" / "Dudas" markers over the pulse chart
};

/** Status pill: Nunito 800 13, px12 py5, no wrap. Chart variant is 12px, px10 py3. */
export function Badge({ tone: t = "ok", children }: { tone?: BadgeTone; children: ReactNode }) {
  return (
    <span className={cx(
      "inline-block whitespace-nowrap rounded-pill font-display font-extrabold",
      t === "chart" ? "px-2.5 py-[3px] text-body-12" : "px-3 py-[5px] text-body-13", tone[t],
    )}>{children}</span>
  );
}

/** Count pill on summary cards: "14 estudiantes". Nunito 900 15, px14 py6, in a brand colour. */
export function CountPill({ color, children }: { color: BrandColor; children: ReactNode }) {
  return <span className={cx("inline-block rounded-pill px-3.5 py-1.5 font-display text-body-15 font-black", brandBg[color])}>{children}</span>;
}
