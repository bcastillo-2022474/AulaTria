import { sql } from "kysely";
import { db } from "../../../db/index.js";
import type { DoubtGroupRef } from "../domain/source.js";

/** What the generator reads: the text of one material, and the doubt topics of the lesson (by slide). */
export type QuizSource = { materialId: string; text: string; groups: DoubtGroupRef[] };

/**
 * READ: the source of a quiz. Tenant on every table. Without `materialId`: the most recent material with text of
 * THIS lesson, else of any lesson of the same course. With `materialId` (RegenerateQuiz): that material, if it is in
 * the same course. Null when there is no text (QUIZ_NO_MATERIAL).
 */
export async function findQuizSource(schoolId: string, lessonId: string, materialId?: string | null): Promise<QuizSource | null> {
  let materials = db.selectFrom("lesson_material")
    .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "lesson_material.lesson_id").onRef("lesson.school_id", "=", "lesson_material.school_id"))
    .innerJoin("lesson as current", (j) => j.onRef("current.course_id", "=", "lesson.course_id").onRef("current.school_id", "=", "lesson.school_id").on("current.id", "=", lessonId))
    .select(["lesson_material.id", "lesson_material.extracted_text"])
    .where("lesson_material.school_id", "=", schoolId)
    .where("lesson_material.extracted_text", "is not", null)
    .where((eb) => eb(eb.fn("trim", [eb.ref("lesson_material.extracted_text")]), "<>", ""));
  materials = materialId
    ? materials.where("lesson_material.id", "=", materialId)
    : materials.orderBy(sql`(lesson_material.lesson_id = ${lessonId})`, "desc").orderBy("lesson_material.created_at", "desc");
  const material = await materials.executeTakeFirst();
  if (!material?.extracted_text) return null;

  const groups = await db.selectFrom("doubt_group")
    .innerJoin("doubt_round", (j) => j.onRef("doubt_round.id", "=", "doubt_group.round_id").onRef("doubt_round.school_id", "=", "doubt_group.school_id"))
    .leftJoin("doubt", (j) => j.onRef("doubt.group_id", "=", "doubt_group.id").onRef("doubt.school_id", "=", "doubt_group.school_id"))
    .select(["doubt_group.title", "doubt_group.slide_number"]).select((eb) => eb.fn.countAll().as("students"))
    .where("doubt_group.school_id", "=", schoolId).where("doubt_round.lesson_id", "=", lessonId)
    .where("doubt_group.slide_number", "is not", null)
    .groupBy("doubt_group.id")
    .execute();

  return {
    materialId: material.id,
    text: material.extracted_text,
    groups: groups.map((g) => ({ title: g.title, slideNumber: g.slide_number!, students: Number(g.students) })),
  };
}
