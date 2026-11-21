import { cx } from "./cx";

/** Hand-raise queue row: rank (Nunito 900 22), name 17/600, offset 14 at 75%. The first one is accent. */
export function QueueRow({ rank, name, offset, current }: { rank: number; name: string; offset: string; current?: boolean }) {
  return (
    <li className={cx(
      "box-border flex min-h-[60px] list-none items-center gap-4 rounded-row px-[18px]",
      current ? "bg-accent text-ink-deep" : "bg-stage-card text-on-stage",
    )}>
      <span className="type-title w-6 text-title-22">{rank}</span>
      <span className="flex-1 text-body-17 font-semibold">{name}</span>
      <span className="text-body-14 opacity-75">{offset}</span>
    </li>
  );
}
