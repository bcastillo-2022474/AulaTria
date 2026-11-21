import { useState } from "react";
import { useParams } from "react-router";
import { courses, followup } from "@aula/rpc";
import { AppBar, Banner, Button, Card, ColumnChart, Content, Eyebrow, Heading, Screen, StatTile, Text, TextArea } from "../../design-system";
import { SignOutButton } from "../auth/SignOutButton";
import { errorMessage, useAulaMutation, useAulaQuery } from "../../rpc/hooks";
import { useSession } from "../../rpc/session";
import { shortName } from "./format";

/** Docente-C-Perfil: one student's participation in the course, and the teacher's private note. */
export function StudentProfilePage() {
  const { courseId = "", studentId = "" } = useParams();
  const session = useSession();
  const course = useAulaQuery(courses.CoursesService.method.getCourse, { courseId });
  const profile = useAulaQuery(followup.FollowupService.method.getStudentParticipation, { courseId, studentId });
  const p = profile.data;
  const back = <Button href={`/courses/${courseId}/students`} variant="link" className="self-start">‹ Estudiantes</Button>;

  return (
    <Screen>
      <AppBar
        left={course.data?.course ? (course.data.course.code || course.data.course.name).toUpperCase() : undefined}
        right={<>{p ? <span>Últimas {p.lessonsTotal} clases</span> : null}<span>{session?.name}</span><SignOutButton /></>}
      />
      <Content>
        {back}
        {!p ? (
          profile.isError ? (
            <div className="flex flex-col items-start gap-4">
              <Banner tone="danger">{errorMessage(profile.error)}</Banner>
              <Button variant="ghost" size="48" onClick={() => void profile.refetch()}>Reintentar</Button>
            </div>
          ) : (
            <Text as="p" size={16}>Cargando el historial…</Text>
          )
        ) : (
          <div className="flex min-h-0 flex-1 gap-9">
            <div className="flex min-w-0 flex-1 flex-col gap-[22px]">
              <Eyebrow>HISTORIAL DE PARTICIPACIÓN</Eyebrow>
              <Heading size={48}>{shortName(p.name)}</Heading>
              <div className="grid grid-cols-2 gap-3.5">
                <StatTile color="danger" value={`${p.lessonsAttended}/${p.lessonsTotal}`} caption="clases con asistencia" />
                <StatTile color="info" value={String(p.doubtsSent)} caption="dudas enviadas" />
                <StatTile color="sun" value={String(p.turnsTaken)} caption={`veces tomó la palabra, ${p.turnsCorrect} acertó`} />
                <StatTile color="ok-deep" value={`${p.quizzesAnswered}/${p.quizzesTotal}`} caption="quizzes en los que participó" />
              </div>
              <Card padding={20} className="flex flex-col gap-2.5">
                <Text size={14}>Interacciones por clase</Text>
                <ColumnChart values={p.perLesson.map((l) => l.interactions)} labels={p.perLesson.map((l) => String(l.number))} />
              </Card>
            </div>
            <NotePanel key={p.studentId} courseId={courseId} studentId={p.studentId} saved={p.note} />
          </div>
        )}
      </Content>
    </Screen>
  );
}

/** "Tu criterio cuenta" + the private note. Remounted per student (key) so the draft starts from the saved note. */
function NotePanel({ courseId, studentId, saved }: { courseId: string; studentId: string; saved: string }) {
  const [draft, setDraft] = useState(saved);
  const save = useAulaMutation(followup.FollowupService.method.saveStudentNote);
  const changed = draft.trim() !== saved.trim() && draft.trim().length > 0;

  return (
    <div className="flex w-[350px] flex-none flex-col gap-4">
      <Heading as="h2" size={24}>Tu criterio cuenta</Heading>
      <Text as="p" size={14} className="leading-normal">
        Esto muestra participación en la app, no comprensión. Quien no usa el celular o prefiere no hablar también aprende.
      </Text>
      <TextArea label="NOTA DEL CATEDRÁTICO" value={draft} maxLength={2000} onChange={(e) => { setDraft(e.target.value); save.reset(); }} />
      {save.error ? <Banner tone="danger">{errorMessage(save.error)}</Banner> : null}
      {save.isSuccess && !changed ? <Banner tone="ok">Nota guardada. Solo tú la ves.</Banner> : null}
      <Button size="54" block disabled={!changed || save.isPending} onClick={() => save.mutate({ courseId, studentId, body: draft })}>
        {save.isPending ? "Guardando…" : "Guardar nota"}
      </Button>
    </div>
  );
}
