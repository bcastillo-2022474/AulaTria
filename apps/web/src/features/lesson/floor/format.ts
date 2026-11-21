/** Advisory turn length the server gives a responder (floor.proto: "advisory 15 s countdown"). */
export const TURN_SECONDS = 15;

/** "+0,8 s": the offset is milliseconds after the round opened, shown with one decimal and a comma. */
export function formatOffset(offsetMs: number): string {
  return `+${(offsetMs / 1000).toFixed(1).replace(".", ",")} s`;
}
