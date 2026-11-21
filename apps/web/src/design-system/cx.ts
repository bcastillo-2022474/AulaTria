/** Joins class names, skipping falsy values. */
export function cx(...parts: Array<string | false | null | undefined>): string {
  return parts.filter(Boolean).join(" ");
}

/** Colour slots shared by action tiles, quiz options and count pills (the four brand dots). */
export type BrandColor = "danger" | "info" | "sun" | "ok-deep";

export const brandBg: Record<BrandColor, string> = {
  danger: "bg-danger text-on-stage",
  info: "bg-info text-on-stage",
  sun: "bg-sun text-ink-deep",
  "ok-deep": "bg-ok-deep text-on-stage",
};
