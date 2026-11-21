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

/** The AulaTria mark (docs/brand/logo-icon.svg, served from public/). Replace the file to change it; `label` is the accessible name. */
export function LogoSlot({ label = "AulaTria" }: { label?: string }) {
  return (
    <img data-slot="brand-logo" src={`${import.meta.env.BASE_URL}logo-icon.svg`} alt={label} className="h-10 w-10 rounded-letter ring-2 ring-white/25" />
  );
}
