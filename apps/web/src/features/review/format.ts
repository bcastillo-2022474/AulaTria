/** Minute index of the class to "29:00". */
export function clock(minute: number): string {
  return `${String(minute).padStart(2, "0")}:00`;
}

/** Seconds to "50:00". */
export function clockSeconds(total: number): string {
  const m = Math.floor(total / 60);
  const s = total % 60;
  return `${String(m).padStart(2, "0")}:${String(s).padStart(2, "0")}`;
}

/** "Ana Lucía Méndez" -> "Ana Lucía M." (the design shows the family name as an initial). */
export function shortName(name: string): string {
  const parts = name.trim().split(/\s+/);
  if (parts.length < 2) return name;
  return `${parts.slice(0, -1).join(" ")} ${parts[parts.length - 1][0]}.`;
}
