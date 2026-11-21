import { useState, type ReactNode } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import { courses, lessons } from "@aula/rpc";
import {
  AppBar, Banner, Button, Card, Content, Eyebrow, Heading, ListGroup, ListRow, Screen, Text,
} from "../../design-system";
import { SignOutButton } from "../auth/SignOutButton";
import { errorMessage, useAulaMutation, useAulaQuery } from "../../rpc/hooks";
import { useSession } from "../../rpc/session";
import { formatShortDate, plural } from "./format";

/** true when the course was just created (CreateCoursePage passes `{ created: true }` as router state). */
function wasJustCreated(state: unknown): boolean {
  return typeof state === "object" && state !== null && "created" in state && state.created === true;
}

/** Docente-C-Curso: join code, open the room, past lessons. */
export function CoursePage() {
  const { courseId = "" } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const session = useSession();
  const [copied, setCopied] = useState(false);

  const detail = useAulaQuery(courses.CoursesService.method.getCourse, { courseId });
  // Opens the room, then the lesson screen (not built yet: contributors add /lessons/:lessonId/room).
  const openRoom = useAulaMutation(lessons.LessonsService.method.openRoom, {
    onSuccess: (res) => {
      const id = res.lesson?.id;
      if (id) navigate(`/lessons/${id}/room`);
    },
  });

  const course = detail.data?.course;
  const back = <Button href="/courses" variant="link">‹ Mis cursos</Button>;

  async function copyJoinCode(code: string) {
    try {
      await navigator.clipboard.writeText(code);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    } catch {
      setCopied(false);
    }
  }

  let body: ReactNode;
  if (!course) {
    body = detail.isError ? (
      <div className="flex flex-col items-start gap-4">
        <Banner tone="danger">{errorMessage(detail.error)}</Banner>
        <Button variant="ghost" size="48" onClick={() => void detail.refetch()}>Reintentar</Button>
      </div>
    ) : (
      <Text as="p" size={16}>Cargando el curso…</Text>
    );
  } else {
    body = (
      <div className="flex min-h-0 flex-1 gap-10">
        <div className="flex min-w-0 flex-1 flex-col gap-6">
          {wasJustCreated(location.state) ? (
            <Banner tone="ok">Curso creado. Comparte este código con tus estudiantes.</Banner>
          ) : null}
          <div className="flex flex-col gap-1.5">
            <Heading size={52}>{course.name}</Heading>
            <Text size={16}>
              {course.code ? `${course.code} · ` : ""}
              {plural(course.studentCount, "estudiante", "estudiantes")}
            </Text>
          </div>

          <div className="flex max-w-[420px] flex-col gap-4">
            <Button size="68" block onClick={() => openRoom.mutate({ courseId })} disabled={openRoom.isPending}>
              {openRoom.isPending ? "Abriendo sala…" : "Iniciar sala"}
            </Button>
            <Button href={`/courses/${courseId}/students`} variant="ghost" size="52" block>Ver estudiantes</Button>
            {openRoom.error ? <Banner tone="danger">{errorMessage(openRoom.error)}</Banner> : null}
            <Card padding={22} className="flex items-center justify-between gap-4">
              <div className="flex flex-col gap-0.5">
                <Eyebrow>CÓDIGO PARA UNIRSE</Eyebrow>
                <Heading as="span" size={28} className="tracking-[0.06em]">{course.joinCode}</Heading>
              </div>
              <Button variant="ghost" size="48" onClick={() => void copyJoinCode(course.joinCode)}>
                {copied ? "Copiado" : "Copiar código"}
              </Button>
            </Card>
          </div>
        </div>

        <div className="flex w-[440px] shrink-0 flex-col gap-3">
          <Eyebrow>CLASES ANTERIORES</Eyebrow>
          {course.lessons.length === 0 ? (
            <Card padding={22}><Text as="p">Todavía no hay clases anteriores.</Text></Card>
          ) : (
            <ListGroup>
              {course.lessons.map((lesson) => (
                <ListRow
                  key={lesson.id}
                  chevron
                  href={`/lessons/${lesson.id}/review`}
                  title={`Clase ${lesson.number}`}
                  subtitle={[
                    lesson.date ? formatShortDate(lesson.date.seconds) : null,
                    plural(lesson.attendeeCount, "asistente", "asistentes"),
                  ].filter(Boolean).join(" · ")}
                />
              ))}
            </ListGroup>
          )}
        </div>
      </div>
    );
  }

  return (
    <Screen>
      <AppBar left={session?.schoolName} right={<><span>{session?.name}</span><SignOutButton /></>} />
      <Content>
        {back}
        {body}
      </Content>
    </Screen>
  );
}
