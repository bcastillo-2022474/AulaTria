import type { ReactNode } from "react";
import { cx } from "./cx";

/**
 * Page frame. "stage" is the deep-purple surface used by ALL teacher/admin screens.
 * `frame` pins it to the design artboard (1280x800) for the gallery and screenshots.
 */
export function Screen({ tone = "stage", frame = false, className, children }: {
  tone?: "stage" | "paper"; frame?: boolean; className?: string; children: ReactNode;
}) {
  return (
    <div className={cx(
      "box-border flex flex-col font-body",
      tone === "stage" ? "bg-stage text-on-stage" : "surface-paper bg-paper text-ink",
      frame ? "h-[800px] w-[1280px] overflow-hidden" : "min-h-full",
      className,
    )}>
      {children}
    </div>
  );
}

/** Area under the AppBar: padding 28 40 32, gap 24 (every screen). */
export function Content({ className, children }: { className?: string; children: ReactNode }) {
  return <main className={cx("box-border flex min-h-0 flex-1 flex-col gap-6 px-10 pt-7 pb-8", className)}>{children}</main>;
}
