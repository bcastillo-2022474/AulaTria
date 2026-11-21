/** Spanish plural: plural(1, "estudiante", "estudiantes") -> "1 estudiante". */
export function plural(n: number, one: string, many: string): string {
  return `${n} ${n === 1 ? one : many}`;
}

const shortDate = new Intl.DateTimeFormat("es", { day: "numeric", month: "short" });

/** Protobuf Timestamp (seconds as bigint) to "18 nov". */
export function formatShortDate(seconds: bigint): string {
  return shortDate.format(new Date(Number(seconds) * 1000));
}
