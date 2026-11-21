import { useEffect, useMemo, useState } from "react";
import { timestampDate, type Timestamp } from "@bufbuild/protobuf/wkt";

/**
 * Time-boxed things (doubt round, quiz, floor turn, QR code) are deadlines, never "seconds left". Count down to the
 * deadline using the server's clock: pass the snapshot's `serverTime` so a wrong laptop clock does not matter.
 *
 *   const left = useCountdown(round.closesAt, snapshot.serverTime);   // seconds, 0 when over
 */
export function useCountdown(deadline: Timestamp | undefined, serverTime: Timestamp | undefined): number {
  // Server clock minus local clock, measured ONCE when this serverTime arrives (a new snapshot = a new object). Recomputing it
  // on every render would pin the countdown to the old serverTime and freeze it between snapshots.
  const skew = useMemo(() => (serverTime ? timestampDate(serverTime).getTime() - Date.now() : 0), [serverTime]);
  const [now, setNow] = useState(() => Date.now());
  useEffect(() => {
    const t = setInterval(() => setNow(Date.now()), 250);
    return () => clearInterval(t);
  }, []);
  if (!deadline) return 0;
  return Math.max(0, Math.ceil((timestampDate(deadline).getTime() - (now + skew)) / 1000));
}

export const formatClock = (seconds: number) => `${Math.floor(seconds / 60)}:${String(seconds % 60).padStart(2, "0")}`;
