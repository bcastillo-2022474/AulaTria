import type { ReactNode } from "react";
import { courses } from "@aula/rpc";
import { AppBar, Badge, Banner, Button, Content, CourseTile, EmptyState, Heading, Screen, Text, type BrandColor } from "../../design-system";
import { SignOutButton } from "../auth/SignOutButton";
import { errorMessage, useAulaQuery } from "../../rpc/hooks";
import { useSession } from "../../rpc/session";
import { plural } from "./format";

// Tile colours cycle through the four brand colours of the design.
const tileColors: readonly BrandColor[] = ["info", "ok-deep", "danger", "sun"];

/** Docente-C-Cursos (list) and Docente-C-CursosVacio (empty state). */
export function CoursesPage() {
  const session = useSession();
  const list = useAulaQuery(courses.CoursesService.method.listCourses);
  const items = list.data?.courses;

  let body: ReactNode;
  if (!items) {
    body = list.isError ? (
      <div className="flex flex-col items-start gap-4">
        <Banner tone="danger">{errorMessage(list.error)}</Banner>
        <Button variant="ghost" size="48" onClick={() => void list.refetch()}>Reintentar</Button>
      </div>
    ) : (
      <Text as="p" size={16}>Cargando tus cursos…</Text>
    );
  } else if (items.length === 0) {
    body = (
      <div className="flex flex-1 items-center justify-center">
        <EmptyState
          variant="page"
          title="Crea tu primer curso"
          body="Solo necesitas un nombre. Después compartes un código con tus estudiantes."
          action={<Button href="#/courses/new" size="64" className="min-w-[260px]">Crear curso</Button>}
        />
      </div>
    );
  } else {
    body = (
      <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 xl:grid-cols-3">
        {items.map((course, i) => (
          <div key={course.id} className="relative">
            <CourseTile
              href={`#/courses/${course.id}`}
              color={tileColors[i % tileColors.length]}
              code={course.code}
              name={course.name}
              meta={plural(course.studentCount, "estudiante", "estudiantes")}
            />
            {course.liveLesson ? (
              <span className="pointer-events-none absolute top-5 right-5">
                <Badge tone="ok">EN VIVO</Badge>
              </span>
            ) : null}
          </div>
        ))}
      </div>
    );
  }

  return (
    <Screen>
      <AppBar left={session?.schoolName} right={<><span>{session?.name}</span><SignOutButton /></>} />
      <Content>
        <div className="flex flex-wrap items-center justify-between gap-4">
          <Heading size={48}>Mis cursos</Heading>
          {items && items.length > 0 ? <Button href="#/courses/new" size="56">+ Crear curso</Button> : null}
        </div>
        {body}
      </Content>
    </Screen>
  );
}
