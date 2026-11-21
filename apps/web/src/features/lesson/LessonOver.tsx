import { courses } from "@aula/rpc";
import { Button, Card, Eyebrow, Heading, Text } from "../../design-system";

/** The lesson is not running any more (ended, or the room was closed): say so and go back to the course. */
export function LessonOver({ status, courseId }: { status: courses.LessonStatus; courseId: string }) {
  const cancelled = status === courses.LessonStatus.CANCELLED;
  return (
    <div className="flex w-[640px] max-w-full flex-col gap-6">
      <Card padding={28} className="flex flex-col gap-[18px]">
        <Eyebrow>{cancelled ? "Sala cerrada" : "Clase terminada"}</Eyebrow>
        <Heading size={56}>{cancelled ? "La sala se cerró" : "La clase terminó"}</Heading>
        <Text as="p" size={16}>
          {cancelled ? "La sala se cerró sin empezar la clase." : "Ya no hay rondas abiertas en esta clase."}
        </Text>
        <Button href={`/courses/${courseId}`} size="56" block>Volver al curso</Button>
      </Card>
    </div>
  );
}
