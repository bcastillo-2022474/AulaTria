import { useParams } from "react-router";
import { courses } from "@aula/rpc";
import { AppBar, Banner, Button, Content, Eyebrow, Heading, ListGroup, ListRow, Screen, Text } from "../../design-system";
import { SignOutButton } from "../auth/SignOutButton";
import { plural } from "../courses/format";
import { errorMessage, useAulaQuery } from "../../rpc/hooks";
import { useSession } from "../../rpc/session";

/** Roster of a course (ListCourseStudents): the way into Docente-C-Perfil. Not in docs/design, built from design-system parts only. */
export function StudentsPage() {
  const { courseId = "" } = useParams();
  const session = useSession();
  const course = useAulaQuery(courses.CoursesService.method.getCourse, { courseId });
  const roster = useAulaQuery(courses.CoursesService.method.listCourseStudents, { courseId });
  const students = roster.data?.students ?? [];

  return (
    <Screen>
      <AppBar left={session?.schoolName} right={<><span>{session?.name}</span><SignOutButton /></>} />
      <Content>
        <Button href={`/courses/${courseId}`} variant="link" className="self-start">‹ {course.data?.course?.name ?? "Curso"}</Button>
        <div className="flex flex-col gap-1.5">
          <Eyebrow>HISTORIAL DE PARTICIPACIÓN</Eyebrow>
          <Heading size={48}>Estudiantes</Heading>
          {roster.data ? <Text size={16}>{plural(students.length, "estudiante", "estudiantes")}</Text> : null}
        </div>
        {roster.isError ? (
          <div className="flex flex-col items-start gap-4">
            <Banner tone="danger">{errorMessage(roster.error)}</Banner>
            <Button variant="ghost" size="48" onClick={() => void roster.refetch()}>Reintentar</Button>
          </div>
        ) : !roster.data ? (
          <Text as="p" size={16}>Cargando estudiantes…</Text>
        ) : (
          <ListGroup className="max-w-[720px]">
            {students.map((s) => (
              <ListRow
                key={s.id}
                chevron
                href={`/courses/${courseId}/students/${s.id}`}
                title={s.name}
                subtitle={`Asistencia ${Math.round(s.attendanceRate * 100)}%`}
              />
            ))}
          </ListGroup>
        )}
      </Content>
    </Screen>
  );
}
