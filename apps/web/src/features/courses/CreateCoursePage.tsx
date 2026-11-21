import { useState, type FormEvent } from "react";
import { useNavigate } from "react-router";
import { courses } from "@aula/rpc";
import { AppBar, Banner, Button, Content, Field, Heading, Screen, Text } from "../../design-system";
import { SignOutButton } from "../auth/SignOutButton";
import { errorMessage, useAulaMutation } from "../../rpc/hooks";
import { useSession } from "../../rpc/session";

/** Docente-C-CrearCurso. On success opens the new course, which shows its join code. */
export function CreateCoursePage() {
  const navigate = useNavigate();
  const session = useSession();
  const [name, setName] = useState("");
  const [code, setCode] = useState("");

  const create = useAulaMutation(courses.CoursesService.method.createCourse, {
    onSuccess: (res) => {
      const id = res.course?.id;
      if (id) navigate(`/courses/${id}`, { state: { created: true } });
    },
  });

  function submit(e: FormEvent<HTMLFormElement>) {
    e.preventDefault();
    create.mutate({ name: name.trim(), code: code.trim() });
  }

  return (
    <Screen>
      <AppBar left={session?.schoolName} right={<><span>Nuevo curso</span><SignOutButton /></>} />
      <Content className="items-center justify-center">
        <form onSubmit={submit} className="flex w-[620px] max-w-full flex-col gap-[22px]">
          <Heading size={48}>Crear curso</Heading>
          <Field
            label="Nombre del curso" size="64" required maxLength={100} autoFocus
            value={name} onChange={(e) => setName(e.target.value)}
          />
          <Field
            label="Código del curso (opcional)" size="64" maxLength={20}
            value={code} onChange={(e) => setCode(e.target.value)}
          />
          <Text size={14}>Tus estudiantes verán este nombre al unirse.</Text>
          {create.error ? <Banner tone="danger">{errorMessage(create.error)}</Banner> : null}
          <div className="mt-2 flex gap-3">
            <Button type="submit" size="60" className="flex-1" disabled={name.trim() === "" || create.isPending}>
              {create.isPending ? "Creando…" : "Crear curso"}
            </Button>
            <Button href="/courses" variant="ghost" size="60">Cancelar</Button>
          </div>
        </form>
      </Content>
    </Screen>
  );
}
