import type { lessons } from "@aula/rpc";

/** The AppBar label of every live-class screen: "BASES DE DATOS 2 · CLASE 12". */
export function lessonLabel(snapshot: lessons.LessonSnapshot | undefined): string | undefined {
  return snapshot ? `${snapshot.courseName} · Clase ${snapshot.number}`.toUpperCase() : undefined;
}
