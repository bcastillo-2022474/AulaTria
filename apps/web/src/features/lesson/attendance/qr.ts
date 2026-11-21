import { encode } from "uqr";

/** The matrix `QrFrame` draws ('1' = dark module, one string per row), for a REAL, scannable QR of `text`. No quiet zone: the frame's padding is it. */
export function qrCells(text: string): string[] {
  return encode(text, { ecc: "M", border: 0 }).data.map((row) => row.map((dark) => (dark ? "1" : "0")).join(""));
}
