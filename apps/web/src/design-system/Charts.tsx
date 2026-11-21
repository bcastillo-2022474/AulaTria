import { Badge } from "./Badge";
import { Eyebrow, Heading } from "./Typography";

/** Pulse bars scale: 1.2px per percentage point (84% -> 101px, as in the design, inferred). Good if >= 55%. */
const PX_PER_PCT = 1.2;
const LOST_BELOW = 55;

export interface PulseMarker { at: number; label: string }

/** Pulse-over-time chart: one bar per minute, ok/warn colour, event markers on top, white playhead. */
export function PulseChart({ values, markers = [], playhead, title = "Pulso de comprensión", caption = "Cada barra es un minuto", className }: {
  values: readonly number[]; markers?: readonly PulseMarker[]; playhead?: number; title?: string; caption?: string; className?: string;
}) {
  return (
    <div className={`flex min-h-0 flex-1 flex-col gap-2.5 ${className ?? ""}`}>
      <div className="flex items-baseline justify-between">
        <Heading as="span" size={20}>{title}</Heading>
        <Eyebrow className="tracking-normal">{caption}</Eyebrow>
      </div>
      <div className="relative flex-1" role="img" aria-label={`${title}: ${values.length} minutos`}>
        <div className="absolute inset-x-0 top-[30px] bottom-0 flex items-end gap-[3px]">
          {values.map((v, i) => (
            <div key={i} className={`flex-1 rounded-bar ${v >= LOST_BELOW ? "bg-ok" : "bg-warn"}`} style={{ height: `${v * PX_PER_PCT}px` }} />
          ))}
        </div>
        {markers.map((m) => (
          <span key={`${m.at}-${m.label}`} className="absolute top-0 -translate-x-1/2" style={{ left: `${m.at}%` }}><Badge tone="chart">{m.label}</Badge></span>
        ))}
        {playhead !== undefined ? <div aria-hidden="true" className="absolute top-7 bottom-0 w-[3px] rounded-[2px] bg-on-stage" style={{ left: `${playhead}%` }} /> : null}
      </div>
    </div>
  );
}

/** Interactions-per-class columns: 11px per count, accent, radius 8; zero = dashed placeholder. */
export function ColumnChart({ values, labels, height = 120 }: { values: readonly number[]; labels: readonly string[]; height?: number }) {
  return (
    <div className="flex items-end gap-2" style={{ height }} role="img" aria-label="Interacciones por clase">
      {values.map((v, i) => (
        <div key={i} className="flex flex-1 flex-col items-center justify-end gap-1.5">
          {v === 0
            ? <div className="box-border h-1.5 w-full rounded-[3px] border-2 border-dashed border-stage-ink" />
            : <div className="w-full rounded-column bg-accent" style={{ height: `${v * 11}px` }} />}
          <span className="text-body-12 text-stage-ink">{labels[i]}</span>
        </div>
      ))}
    </div>
  );
}
