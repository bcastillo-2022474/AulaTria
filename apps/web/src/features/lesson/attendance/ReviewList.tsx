import { attendance } from "@aula/rpc";
import { Badge, Banner, Button, Card, Text } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useResolveAttendance } from "../../../rpc/gen/hooks";

const reasonText = (r: attendance.ReviewItem) =>
  r.reason === attendance.ReviewReason.TOO_FAR ? `Escaneó a unos ${r.distanceM} m del aula`
    : r.reason === attendance.ReviewReason.NO_LOCATION ? "Sin permiso de ubicación"
      : r.reason === attendance.ReviewReason.LOW_ACCURACY ? "Ubicación poco precisa"
        : "";

/**
 * "Por revisar": every student whose scan needed the teacher. Pending ones offer "Aceptar" / "Ausente" (ResolveAttendance);
 * decided ones stay listed with the outcome. Works while the round is open and after it closed.
 */
export function ReviewList({ round }: { round: attendance.AttendanceRound }) {
  const resolve = useResolveAttendance();
  const decide = (studentId: string, decision: attendance.Decision) => resolve.mutate({ roundId: round.id, studentId, decision });
  return (
    <div className="flex min-h-0 flex-col gap-3.5">
      {round.review.length === 0 ? <Text as="p" size={16}>Nadie quedó por revisar.</Text> : null}
      {round.review.map((r) => (
        <Card key={r.studentId} padding={18} className="flex items-center gap-4">
          <div className="flex flex-1 flex-col gap-0.5">
            <span className="text-body-17 font-semibold">{r.name}</span>
            <Text size={14}>{reasonText(r)}</Text>
          </div>
          {r.status === attendance.AttendanceStatus.NEEDS_REVIEW ? (
            <>
              <Button size="48" disabled={resolve.isPending} onClick={() => decide(r.studentId, attendance.Decision.ACCEPT)}>Aceptar</Button>
              <Button size="48" variant="ghost" disabled={resolve.isPending} onClick={() => decide(r.studentId, attendance.Decision.REJECT)}>Ausente</Button>
            </>
          ) : (
            <Badge tone={r.status === attendance.AttendanceStatus.PRESENT ? "ok" : "chart"}>
              {r.status === attendance.AttendanceStatus.PRESENT ? "Aceptado" : "Ausente"}
            </Badge>
          )}
        </Card>
      ))}
      {resolve.error ? <Banner tone="danger">{errorMessage(resolve.error)}</Banner> : null}
    </div>
  );
}

/** Pending = still waiting for the teacher. */
export const pendingCount = (round: attendance.AttendanceRound) => round.review.filter((r) => r.status === attendance.AttendanceStatus.NEEDS_REVIEW).length;
/** Enrolled students who never scanned (no record): absent by derivation. In the review list = scanned and needed review. */
export const noRecordCount = (round: attendance.AttendanceRound) => Math.max(0, round.enrolledCount - round.inRoomCount - round.review.length);
