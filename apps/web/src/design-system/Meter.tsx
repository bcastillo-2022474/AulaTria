import { cx } from "./cx";

/** Two-tone meter (good / lost): 18px, radius 9, 2px gap. `value` 0..100 is the good share. */
export function Meter({ value, lost, label, className }: { value: number; lost?: number; label: string; className?: string }) {
  const bad = lost ?? 100 - value;
  return (
    <div role="meter" aria-label={label} aria-valuenow={Math.round(value)} aria-valuemin={0} aria-valuemax={100}
      className={cx("flex h-[18px] gap-0.5 overflow-hidden rounded-meter bg-stage-track", className)}>
      <div className="bg-ok" style={{ width: `${value}%` }} />
      <div className="bg-warn" style={{ width: `${bad}%` }} />
    </div>
  );
}

/** Thin accent progress bar: 12px, radius 6 (countdowns, "Responde ahora", summary cards). */
export function Progress({ value, label, className }: { value: number; label: string; className?: string }) {
  return (
    <div role="progressbar" aria-label={label} aria-valuenow={Math.round(value)} aria-valuemin={0} aria-valuemax={100}
      className={cx("h-3 rounded-progress bg-stage-track", className)}>
      <div className="h-3 rounded-progress bg-accent" style={{ width: `${value}%` }} />
    </div>
  );
}
