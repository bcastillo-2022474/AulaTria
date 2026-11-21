import type { ReactNode } from "react";
import { type BrandColor, brandBg, cx } from "./cx";
import { Heading } from "./Typography";

interface TileBase { color: BrandColor; href?: string; onClick?: () => void; className?: string; }

function TileShell({ color, href, onClick, className, children }: TileBase & { children: ReactNode }) {
  const cls = cx("box-border flex flex-col rounded-card border-b-8 border-edge-shade no-underline", brandBg[color], className);
  if (href !== undefined) return <a href={href} className={cls}>{children}</a>;
  return <button type="button" onClick={onClick} className={cx(cls, "cursor-pointer border-0 border-b-8 text-left")}>{children}</button>;
}

/** Letter badge: 32px rounded square on action tiles. */
export function LetterBadge({ letter, shape = "square" }: { letter: string; shape?: "square" | "circle" }) {
  return shape === "square" ? (
    <span aria-hidden="true" className="flex size-8 items-center justify-center rounded-letter bg-stage-chip font-display text-body-14 font-extrabold">{letter}</span>
  ) : (
    <span aria-hidden="true" className="relative flex size-[34px] items-center justify-center rounded-full bg-stage-chip font-display text-body-17 font-extrabold">{letter}</span>
  );
}

/** Live screen tile: letter badge, title (Nunito 28) pushed to bottom, subtitle. Min-height 200, padding 24. */
export function ActionTile({ letter, title, subtitle, ...base }: TileBase & { letter: string; title: string; subtitle: string }) {
  return (
    <TileShell {...base} className={cx("min-h-[200px] gap-3.5 p-6", base.className)}>
      <div className="flex items-center justify-between"><LetterBadge letter={letter} /></div>
      <Heading as="span" size={28} className="mt-auto">{title}</Heading>
      <span className="text-body-14 leading-[1.35] opacity-90">{subtitle}</span>
    </TileShell>
  );
}

/** Courses grid tile: code, name (Nunito 32) pushed to bottom, student count. Min-height 220, padding 26. */
export function CourseTile({ code, name, meta, ...base }: TileBase & { code: string; name: string; meta: string }) {
  return (
    <TileShell {...base} className={cx("min-h-[220px] gap-1.5 p-[26px]", base.className)}>
      <span className="font-display text-body-15 font-extrabold">{code}</span>
      <Heading as="span" size={32} className="mt-auto">{name}</Heading>
      <span className="text-body-15">{meta}</span>
    </TileShell>
  );
}

/** Profile stat tile: radius 22, edge 6, value Nunito 40 + caption 14. */
export function StatTile({ color, value, caption }: { color: BrandColor; value: string; caption: string }) {
  return (
    <div className={cx("box-border flex flex-col gap-1 rounded-stat border-b-6 border-edge-shade px-[22px] py-5", brandBg[color])}>
      <Heading as="span" size={40}>{value}</Heading>
      <span className="text-body-14 opacity-90">{caption}</span>
    </div>
  );
}
