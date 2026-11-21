import type { InputHTMLAttributes, TextareaHTMLAttributes } from "react";
import { useId } from "react";
import { cx } from "./cx";

interface FieldCommon { label: string; hint?: string; error?: string }

export type FieldProps = FieldCommon & Omit<InputHTMLAttributes<HTMLInputElement>, "size"> & { size?: "60" | "64" };

/** White input on stage: radius 18, Nunito 700 20 (22 at height 64), ink-deep text. Read-only = 70% white. */
export function Field({ label, hint, error, size = "60", id, className, readOnly, ...rest }: FieldProps) {
  const auto = useId();
  const fieldId = id ?? auto;
  const noteId = `${fieldId}-note`;
  return (
    <div className="flex flex-col gap-2">
      <label htmlFor={fieldId} className="text-body-14 font-bold text-stage-ink">{label}</label>
      <input
        id={fieldId}
        readOnly={readOnly}
        aria-invalid={error ? true : undefined}
        aria-describedby={error || hint ? noteId : undefined}
        {...rest}
        className={cx(
          "box-border w-full rounded-control border-0 px-[22px] font-display font-bold text-ink-deep placeholder:text-ink-deep/50",
          size === "60" ? "h-[60px] text-body-20" : "h-16 text-body-22",
          readOnly ? "bg-stage-readonly" : "bg-surface",
          error && "ring-[3px] ring-warn",
          className,
        )}
      />
      {error ? <span id={noteId} role="alert" className="text-body-14 font-bold text-warn">{error}</span>
        : hint ? <span id={noteId} className="text-body-14 text-stage-ink">{hint}</span> : null}
    </div>
  );
}

/** Notes box (Perfil): translucent, radius 20, Inter 15/1.5, no resize. Label is the 13px caps eyebrow. */
export function TextArea({ label, id, className, ...rest }: FieldCommon & TextareaHTMLAttributes<HTMLTextAreaElement>) {
  const auto = useId();
  const fieldId = id ?? auto;
  return (
    <>
      <label htmlFor={fieldId} className="type-eyebrow">{label}</label>
      <textarea
        id={fieldId}
        {...rest}
        className={cx("box-border min-h-0 flex-1 resize-none rounded-textarea border-0 bg-stage-card p-4 font-body text-body-15 leading-normal font-normal text-on-stage", className)}
      />
    </>
  );
}
