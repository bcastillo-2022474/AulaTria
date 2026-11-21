import { useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import { courses } from "@aula/rpc";
import { AppBar, Banner, Button, Card, Content, Eyebrow, Heading, Meter, Screen, Stat, Text } from "../../design-system";
import { errorMessage, useAulaQuery } from "../../rpc/hooks";
import { useCloseRoom, useStartLesson } from "../../rpc/gen/hooks";
import { plural } from "../courses/format";
import { LessonOver } from "./LessonOver";
import { lessonLabel } from "./format";
import { useLessonLive } from "./useLessonLive";

/** Docente-C-SalaAbierta: the lobby. How many students have joined, "Empezar clase" (-> live) or "Cerrar sala" (-> course). */
export function RoomPage() {
  const { lessonId = "" } = useParams();
  const navigate = useNavigate();
  const { snapshot, query } = useLessonLive(lessonId);
  // The join code lives on the course, not in the snapshot.
  const course = useAulaQuery(courses.CoursesService.method.getCourse, { courseId: snapshot?.courseId ?? "" }, { enabled: !!snapshot?.courseId });

  const start = useStartLesson({ onSuccess: () => navigate(`/lessons/${lessonId}/live`, { replace: true }) });
  const close = useCloseRoom({ onSuccess: () => navigate(`/courses/${snapshot?.courseId ?? ""}`, { replace: true }) });

  // The class can start from another tab or device: whenever the snapshot says LIVE, this screen is the wrong one.
  const live = snapshot?.status === courses.LessonStatus.LIVE;
  useEffect(() => {
    if (live) navigate(`/lessons/${lessonId}/live`, { replace: true });
  }, [live, lessonId, navigate]);

  let body;
  if (!snapshot) {
    body = query.isError ? (
      <div className="flex flex-col items-start gap-4">
        <Banner tone="danger">{errorMessage(query.error)}</Banner>
        <Button variant="ghost" size="48" onClick={() => void query.refetch()}>Reintentar</Button>
      </div>
    ) : (
      <Text as="p" size={16}>Cargando la sala…</Text>
    );
  } else if (snapshot.status !== courses.LessonStatus.LOBBY) {
    body = live ? <Text as="p" size={16}>Entrando a la clase…</Text> : <LessonOver status={snapshot.status} courseId={snapshot.courseId} />;
  } else {
    const share = snapshot.enrolledCount > 0 ? (snapshot.joinedCount / snapshot.enrolledCount) * 100 : 0;
    const joinCode = course.data?.course?.joinCode;
    const busy = start.isPending || close.isPending;
    const failure = start.error ?? close.error;
    body = (
      <div className="flex min-h-0 flex-1 gap-10">
        <div className="flex flex-1 flex-col justify-center gap-6">
          <Eyebrow>SALA ABIERTA · {snapshot.courseName.toUpperCase()}</Eyebrow>
          <Heading size={56} className="leading-[1.05]">Tus estudiantes ya pueden entrar</Heading>
          <Text as="p" size={18} className="max-w-[520px]">
            Les llega un aviso y entran con un toque.
            {joinCode ? <> Si alguien no lo recibe, puede usar el código <b className="text-on-stage">{joinCode}</b>.</> : null}
          </Text>
          {failure ? <Banner tone="danger" className="max-w-[520px]">{errorMessage(failure)}</Banner> : null}
          <div className="flex max-w-[520px] gap-3">
            <Button size="64" className="flex-1 text-body-19" disabled={busy} onClick={() => start.mutate({ lessonId })}>
              {start.isPending ? "Empezando…" : "Empezar clase"}
            </Button>
            <Button variant="ghost" size="64" disabled={busy} onClick={() => close.mutate({ lessonId })}>
              {close.isPending ? "Cerrando…" : "Cerrar sala"}
            </Button>
          </div>
        </div>
        <div className="flex w-[430px] shrink-0 flex-col justify-center">
          <Card padding={28} className="flex flex-col gap-[18px]">
            <Stat value={String(snapshot.joinedCount)} size={88} caption={`de ${snapshot.enrolledCount} se han unido`} />
            <Meter value={share} lost={0} label="Estudiantes que se han unido" />
            <Text size={16}>{plural(snapshot.connectedCount, "conectado ahora", "conectados ahora")}</Text>
          </Card>
        </div>
      </div>
    );
  }

  return (
    <Screen>
      <AppBar left={lessonLabel(snapshot)} right={<span>Sala abierta</span>} />
      <Content>{body}</Content>
    </Screen>
  );
}
