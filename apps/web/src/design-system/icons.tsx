import type { SVGProps } from "react";

type IconProps = Omit<SVGProps<SVGSVGElement>, "children"> & { size?: number };

/** Play triangle, path copied from Docente-C-Pulso. Fill follows currentColor. */
export function PlayIcon({ size = 24, ...rest }: IconProps) {
  return (
    <svg width={size} height={size} viewBox="0 0 24 24" aria-hidden="true" {...rest}>
      <path d="M8 5v14l11-7z" fill="currentColor" />
    </svg>
  );
}

/** The design uses text glyphs, not SVGs, for these two. Kept as components so the size is a token. */
export function ChevronGlyph() {
  return <span aria-hidden="true" className="text-body-26 text-stage-ink">›</span>;
}
export function CheckGlyph() {
  return <span aria-hidden="true" className="text-body-20 font-extrabold">✓</span>;
}
