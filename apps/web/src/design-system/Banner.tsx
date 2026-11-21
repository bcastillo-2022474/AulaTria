import type { ReactNode } from "react";
import { cx } from "./cx";

type BannerTone = "ok" | "warn" | "danger";
// ok is from the design (Admin-C-Lista). warn/danger are derived with the same shape.
const tone: Record<BannerTone, string> = {
  ok: "bg-ok text-ok-ink",
  warn: "bg-warn text-ink-deep",
  danger: "bg-danger text-on-stage",
};

/** Inline banner / toast: radius 18, px20 py14, Inter 16/700. role=status (alert for danger). */
export function Banner({ tone: t = "ok", children, className }: { tone?: BannerTone; children: ReactNode; className?: string }) {
  return <div role={t === "danger" ? "alert" : "status"} className={cx("rounded-control px-5 py-3.5 text-body-16 font-bold", tone[t], className)}>{children}</div>;
}
