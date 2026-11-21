import type { ReactNode } from "react";

/** 72px top strip: small caps label left ("CC3057 · CLASE 12"), status right. `brand` is the logo slot. */
export function AppBar({ left, right, brand }: { left?: ReactNode; right?: ReactNode; brand?: ReactNode }) {
  return (
    <header className="box-border flex h-[72px] items-center justify-between px-10">
      <span className="flex items-center gap-3 text-body-13 font-extrabold tracking-[0.04em] text-stage-ink">
        {brand}
        {left}
      </span>
      <span className="flex items-center gap-2.5 text-body-14 text-stage-ink">{right}</span>
    </header>
  );
}

/** 8px dot used for "Grabando": warn colour. */
export function StatusDot() {
  return <span aria-hidden="true" className="inline-block size-2 rounded-full bg-warn" />;
}

/** Marked placeholder: replace with docs/brand/logo-icon.png / logo-full.png when the files land. */
export function LogoSlot({ label = "LOGO" }: { label?: string }) {
  return (
    <span data-slot="brand-logo" className="inline-flex h-8 min-w-8 items-center justify-center rounded-letter border-2 border-dashed border-stage-ink px-2 text-body-12 font-extrabold text-stage-ink">
      {label}
    </span>
  );
}
