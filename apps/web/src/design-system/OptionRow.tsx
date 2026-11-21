import type { ReactNode } from "react";
import { CheckGlyph } from "./icons";
import { LetterBadge } from "./Tile";
import { type BrandColor, brandBg, cx } from "./cx";

export type OptionColor = BrandColor;
/** A=danger, B=info, C=sun, D=ok-deep: fixed by the design. */
export const OPTION_COLORS: readonly OptionColor[] = ["danger", "info", "sun", "ok-deep"];

/**
 * Quiz answer row. Nunito 800 17, radius 16, 5px edge, letter circle 34.
 * `count`/`share` show the live vote bar (share 0..1 of the biggest option); `correct` adds the check;
 * `dimmed` is the 50% opacity of non-correct options in the results screen. `height` 60 (live/draft) or 56 (results).
 */
export function OptionRow({ letter, color, text, correct, count, share, dimmed, height = 60, trailing }: {
  letter: string; color: OptionColor; text: string; correct?: boolean; count?: number; share?: number;
  dimmed?: boolean; height?: 56 | 60; trailing?: ReactNode;
}) {
  return (
    <div className={cx(
      "relative box-border flex items-center gap-3.5 overflow-hidden rounded-row border-b-[5px] border-edge-shade px-[18px] font-display text-body-17 font-extrabold",
      height === 60 ? "min-h-[60px]" : "min-h-14", brandBg[color], dimmed && "opacity-50",
    )}>
      {share !== undefined ? <div aria-hidden="true" className="absolute inset-y-0 left-0 bg-stage-chip" style={{ width: `${share * 100}%` }} /> : null}
      <span className="relative"><LetterBadge letter={letter} shape="circle" /></span>
      <span className="relative leading-[1.2]">{text}</span>
      <span className="relative ml-auto flex">
        {trailing ?? (count !== undefined ? <span className="font-body text-body-15 font-semibold">{count}</span> : correct ? <CheckGlyph /> : null)}
      </span>
    </div>
  );
}
