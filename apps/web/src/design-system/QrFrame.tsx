import { cx } from "./cx";
import { QR_SAMPLE } from "./qrSample";

/**
 * White QR frame. `large` = attendance screen (radius 32, padding 30, 8px edge, 360px);
 * default = course-created screen (radius 28, padding 24, 260px, no edge). `cells` are rows of '1'/'0'.
 * Pass the real matrix from the QR encoder; the default is the illustrative sample from the design.
 */
export function QrFrame({ cells = QR_SAMPLE, large = false, label = "Código QR de asistencia (ilustrativo)" }: {
  cells?: readonly string[]; large?: boolean; label?: string;
}) {
  const n = cells.length;
  const path = cells.flatMap((row, y) => [...row].map((c, x) => (c === "1" ? `M${x} ${y}h1v1h-1z` : ""))).join("");
  return (
    <div className={cx("flex bg-surface", large ? "rounded-qr-lg border-b-8 border-edge-shade p-[30px]" : "rounded-qr p-6")}>
      <svg viewBox={`0 0 ${n} ${n}`} width={large ? 360 : 260} height={large ? 360 : 260} role="img" aria-label={label}
        shapeRendering="crispEdges" className="fill-ink-deep">
        <path d={path} />
      </svg>
    </div>
  );
}

/** Big white code box (course code): Nunito 900 72, tracking .08em, radius 26, edge 8. */
export function CodeBox({ code }: { code: string }) {
  return (
    <div className="flex items-center justify-center rounded-card border-b-8 border-edge-shade bg-surface px-7 py-[22px]">
      <span className="font-display text-title-72 leading-none font-black tracking-[0.08em] text-ink-deep">{code}</span>
    </div>
  );
}
