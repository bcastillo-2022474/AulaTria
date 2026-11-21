import { useLocation, useParams } from "react-router";
import { attendance } from "@aula/rpc";
import { AppBar, Banner, Button, Content, Screen, Text } from "../../design-system";
import { errorMessage } from "../../rpc/hooks";
import { useGetAttendanceRound } from "../../rpc/gen/hooks";
import { AttendanceClosed } from "./attendance/AttendanceClosed";
import { AttendanceOpen } from "./attendance/AttendanceOpen";
import { lessonLabel } from "./format";
import { useLessonLive } from "./useLessonLive";

/**
 * Docente-C-Asistencia and Docente-C-AsistCerrada on one route: the round's status decides which one. The round is the cached
 * GetAttendanceRound (polled; the lesson stream refreshes it at once), so the screen turns into the closed one by itself.
 */
export function AttendancePage() {
  const { lessonId = "", roundId = "" } = useParams();
  const { state } = useLocation() as { state: { withoutLocation?: boolean } | null };
  const { snapshot } = useLessonLive(lessonId);
  const query = useGetAttendanceRound({ roundId });
  const round = query.data?.round;
  const open = round?.status === attendance.AttendanceRoundStatus.OPEN;

  let body;
  if (!round) {
    body = query.isError ? (
      <div className="flex flex-col items-start gap-4">
        <Banner tone="danger">{errorMessage(query.error)}</Banner>
        <Button variant="ghost" size="48" onClick={() => void query.refetch()}>Reintentar</Button>
        <Button href={`/lessons/${lessonId}/live`} variant="link">Volver a la clase</Button>
      </div>
    ) : (
      <Text as="p" size={16}>Cargando la asistencia…</Text>
    );
  } else if (open) {
    body = <AttendanceOpen round={round} serverTime={snapshot?.serverTime} withoutLocation={state?.withoutLocation === true} />;
  } else {
    body = <AttendanceClosed round={round} lessonId={lessonId} />;
  }

  return (
    <Screen>
      <AppBar left={lessonLabel(snapshot)} right={<span>{!round ? "Asistencia" : open ? "Asistencia abierta" : "Asistencia"}</span>} />
      <Content>{body}</Content>
    </Screen>
  );
}
