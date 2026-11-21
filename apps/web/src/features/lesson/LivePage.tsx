import { useEffect, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router";
import { courses } from "@aula/rpc";
import { ActionTile, AppBar, Banner, Button, Card, Content, formatClock, Heading, Meter, Screen, Stat, Text } from "../../design-system";
import { errorMessage } from "../../rpc/hooks";
import { useEndLesson, useOpenAttendanceRound, useOpenDoubtRound } from "../../rpc/gen/hooks";
import { useCountdown } from "../../rpc/time";
import { plural } from "../courses/format";
import { LessonOver } from "./LessonOver";
import { teacherPosition } from "./attendance/teacherPosition";
import { lessonLabel } from "./format";
import { useLessonLive } from "./useLessonLive";

/**
 * Docente-C-Live: the class right now. Everything on it comes from the live snapshot: who is connected, the pulse and
 * which rounds are open. Floor, quiz, doubts and attendance open their own screens ("Preguntar dudas" opens a 30 s round, and
 * "Tomar asistencia" opens attendance centred on the teacher's position; each returns to the round already open).
 */
export function LivePage() {
  const { lessonId = "" } = useParams();
  const navigate = useNavigate();
  const { snapshot, query } = useLessonLive(lessonId);
  const [confirmEnd, setConfirmEnd] = useState(false);
  const end = useEndLesson({ onSuccess: () => navigate(`/courses/${snapshot?.courseId ?? ""}`, { replace: true }) });

  const openDoubts = useOpenDoubtRound({ onSuccess: (res) => res.round && navigate(`/lessons/${lessonId}/doubts/${res.round.id}`) });
  // Attendance is centred on where the teacher is: ask the browser first, and open without location if it cannot say.
  const [locating, setLocating] = useState(false);
  const located = useRef(false);
  const openAttendance = useOpenAttendanceRound({
    onSuccess: (res) => res.round && navigate(`/lessons/${lessonId}/attendance/${res.round.id}`, { state: { withoutLocation: !located.current } }),
  });
  const startAttendance = async () => {
    setLocating(true);
    const here = await teacherPosition();
    setLocating(false);
    located.current = here !== null;
    openAttendance.mutate({ lessonId, hasLocation: here !== null, lat: here?.lat ?? 0, lng: here?.lng ?? 0 });
  };
  const attendanceRound = snapshot?.activeAttendanceRound;
  const doubt = snapshot?.activeDoubtRound;
  const doubtLeft = useCountdown(doubt?.closesAt, snapshot?.serverTime);

  // Still in the lobby (e.g. the room was opened from another tab): the lobby screen is the right one.
  const lobby = snapshot?.status === courses.LessonStatus.LOBBY;
  useEffect(() => {
    if (lobby) navigate(`/lessons/${lessonId}/room`, { replace: true });
  }, [lobby, lessonId, navigate]);

  let body;
  if (!snapshot) {
    body = query.isError ? (
      <div className="flex flex-col items-start gap-4">
        <Banner tone="danger">{errorMessage(query.error)}</Banner>
        <Button variant="ghost" size="48" onClick={() => void query.refetch()}>Reintentar</Button>
      </div>
    ) : (
      <Text as="p" size={16}>Cargando la clase…</Text>
    );
  } else if (snapshot.status !== courses.LessonStatus.LIVE) {
    body = lobby ? <Text as="p" size={16}>Abriendo la sala…</Text> : <LessonOver status={snapshot.status} courseId={snapshot.courseId} />;
  } else {
    const pulse = snapshot.pulse;
    const answered = (pulse?.okCount ?? 0) + (pulse?.lostCount ?? 0);
    const closed = snapshot.lastClosedDoubtRound;
    const doubtSubtitle = doubt
      ? `Ronda abierta · ${formatClock(doubtLeft)} · ${plural(doubt.submittedCount, "duda", "dudas")}`
      : closed
        ? `Última ronda cerrada · ${plural(closed.submittedCount, "duda", "dudas")}`
        : "30 segundos";
    body = (
      <>
        <div className="flex flex-col gap-1.5">
          <Heading size={44}>Clase en curso</Heading>
          <Text size={15}>{snapshot.connectedCount} de {snapshot.enrolledCount} conectados</Text>
        </div>

        <Card padding={28} className="flex flex-col gap-4">
          <div className="flex items-baseline justify-between gap-4">
            {answered > 0 ? (
              <Stat value={`${pulse?.okPercent ?? 0}%`} caption="voy bien" />
            ) : (
              <Stat value="—" caption="esperando el pulso de la clase" />
            )}
            <Text size={16}>{plural(pulse?.lostCount ?? 0, "marcó «me perdí»", "marcaron «me perdí»")}</Text>
          </div>
          <Meter
            value={answered > 0 ? (pulse?.okPercent ?? 0) : 0}
            lost={answered > 0 ? 100 - (pulse?.okPercent ?? 0) : 0}
            label="Pulso de la clase"
          />
        </Card>

        <div className="grid min-h-0 flex-1 grid-cols-4 gap-5">
          {doubt ? (
            <ActionTile href={`/lessons/${lessonId}/doubts/${doubt.id}`} color="danger" letter="D" title="Preguntar dudas" subtitle={doubtSubtitle} />
          ) : (
            <ActionTile onClick={() => openDoubts.mutate({ lessonId, durationSeconds: 30 })} color="danger" letter="D" title="Preguntar dudas" subtitle={openDoubts.isPending ? "Abriendo la ronda…" : doubtSubtitle} />
          )}
          <ActionTile
            href={`/lessons/${lessonId}/floor`} color="info" letter="P" title="Dar la palabra"
            subtitle={snapshot.activeFloorRound ? "Ronda en curso" : "El primero en pedirla responde"}
          />
          <ActionTile
            href={`/lessons/${lessonId}/quiz`} color="sun" letter="Q" title="Lanzar quiz"
            subtitle={snapshot.activeQuiz ? "Quiz en curso" : "Pregunta lista desde tu material"}
          />
          {attendanceRound ? (
            <ActionTile
              href={`/lessons/${lessonId}/attendance/${attendanceRound.id}`} color="ok-deep" letter="A" title="Tomar asistencia"
              subtitle={`Abierta · ${plural(attendanceRound.presentCount, "presente", "presentes")} · ${attendanceRound.needsReviewCount} por revisar`}
            />
          ) : (
            <ActionTile
              onClick={() => void startAttendance()} color="ok-deep" letter="A" title="Tomar asistencia"
              subtitle={locating ? "Obteniendo tu ubicación…" : openAttendance.isPending ? "Abriendo la asistencia…" : "QR que cambia cada 8 s"}
            />
          )}
        </div>

        {end.error || openDoubts.error || openAttendance.error ? <Banner tone="danger">{errorMessage(end.error ?? openDoubts.error ?? openAttendance.error)}</Banner> : null}
        <div className="flex items-center justify-end gap-3">
          {closed && !doubt ? <Button href={`/lessons/${lessonId}/doubts/${closed.id}`} variant="ghost" size="52">Ver la última ronda de dudas</Button> : null}
          {confirmEnd ? (
            <>
              <Text size={16} tone="strong">¿Terminar la clase? Se cierran las rondas abiertas.</Text>
              <Button variant="ghost" size="52" disabled={end.isPending} onClick={() => setConfirmEnd(false)}>Seguir en clase</Button>
              <Button size="52" disabled={end.isPending} onClick={() => end.mutate({ lessonId })}>
                {end.isPending ? "Terminando…" : "Sí, terminar clase"}
              </Button>
            </>
          ) : (
            <Button variant="ghost" size="52" onClick={() => setConfirmEnd(true)}>Terminar clase</Button>
          )}
        </div>
      </>
    );
  }

  return (
    <Screen>
      <AppBar left={lessonLabel(snapshot)} right={<span>En vivo</span>} />
      <Content>{body}</Content>
    </Screen>
  );
}
