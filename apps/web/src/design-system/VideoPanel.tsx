import { PlayIcon } from "./icons";

/** Recording placeholder (Docente-C-Pulso): dark well, 68px accent play button with 5px edge, timecode bottom-left. */
export function VideoPanel({ caption, timecode, onPlay }: { caption: string; timecode: string; onPlay?: () => void }) {
  return (
    <div className="relative box-border flex min-w-0 flex-1 items-center justify-center rounded-card bg-stage-well shadow-lift">
      <div className="flex flex-col items-center gap-3.5 text-body-14 text-stage-ink">
        <button type="button" aria-label="Reproducir" onClick={onPlay}
          className="flex size-[68px] cursor-pointer items-center justify-center rounded-full border-0 border-b-[5px] border-accent-edge bg-accent text-ink-deep">
          <PlayIcon />
        </button>
        {caption}
      </div>
      <span className="absolute bottom-4 left-5 text-body-13 text-stage-ink">{timecode}</span>
    </div>
  );
}
