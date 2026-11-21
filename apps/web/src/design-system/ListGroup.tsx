import type { ReactNode } from "react";
import { ChevronGlyph } from "./icons";
import { Card } from "./Card";
import { cx } from "./cx";

/** Card with padding 0 that holds ListRows separated by 1px dividers. */
export function ListGroup({ children, className }: { children: ReactNode; className?: string }) {
  return <Card padding={0} className={cx("overflow-hidden", className)}><ul className="m-0 flex list-none flex-col p-0">{children}</ul></Card>;
}

/**
 * Row inside a ListGroup. density "64" = course history (px20, title 17, chevron);
 * "76" = admin list (px24, title 18, trailing slot). Pass `href` to make the whole row a link.
 */
export function ListRow({ title, subtitle, trailing, chevron, href, density = "64" }: {
  title: string; subtitle: string; trailing?: ReactNode; chevron?: boolean; href?: string; density?: "64" | "76";
}) {
  const wide = density === "76";
  const body = (
    <>
      <span className="flex flex-1 flex-col gap-0.5">
        <span className={cx("font-bold text-on-stage", wide ? "text-body-18" : "text-body-17")}>{title}</span>
        <span className="text-body-14 text-stage-ink">{subtitle}</span>
      </span>
      {trailing ? <span className="flex items-center gap-3.5">{trailing}</span> : null}
      {chevron ? <ChevronGlyph /> : null}
    </>
  );
  const cls = cx(
    "box-border flex items-center text-on-stage no-underline",
    wide ? "min-h-[76px] gap-4 px-6" : "min-h-16 gap-3.5 px-5",
  );
  return <li className="border-t border-stage-divider first:border-t-0">{href !== undefined ? <a href={href} className={cls}>{body}</a> : <div className={cls}>{body}</div>}</li>;
}
