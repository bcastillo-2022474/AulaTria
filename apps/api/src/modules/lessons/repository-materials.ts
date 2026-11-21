import type { Executor } from "../../shared/tx.js";
import { LessonMaterial } from "./domain/material.js";

/**
 * WRITE-SIDE persistence of lesson materials. Concrete class, no interface. Listing materials (ListMaterials) is a
 * read and lives in queries/listMaterials.ts.
 * Used by: UploadMaterial (insert), the extraction step (save).
 */
export class LessonMaterialRepository {
  constructor(private db: Executor) {}

  /** Ownership is part of the lookup: a teacher only loads materials of lessons in their own courses. */
  async findForTeacher(schoolId: string, teacherId: string, materialId: string): Promise<LessonMaterial | null> {
    const r = await this.db.selectFrom("lesson_material")
      .innerJoin("lesson", (j) => j.onRef("lesson.id", "=", "lesson_material.lesson_id").onRef("lesson.school_id", "=", "lesson_material.school_id"))
      .innerJoin("course", (j) => j.onRef("course.id", "=", "lesson.course_id").onRef("course.school_id", "=", "lesson.school_id"))
      .selectAll("lesson_material").where("lesson_material.school_id", "=", schoolId).where("lesson_material.id", "=", materialId)
      .where("course.teacher_id", "=", teacherId).executeTakeFirst();
    return r ? toMaterial(r) : null;
  }

  async insert(m: LessonMaterial): Promise<void> {
    await this.db.insertInto("lesson_material").values({
      id: m.id, school_id: m.schoolId, lesson_id: m.lessonId, file_id: m.fileId, filename: m.filename,
      extracted_text: m.extractedText, created_at: m.createdAt,
    }).execute();
  }

  async save(m: LessonMaterial): Promise<void> {
    await this.db.updateTable("lesson_material").set({ extracted_text: m.extractedText })
      .where("school_id", "=", m.schoolId).where("id", "=", m.id).execute();
  }
}

type MaterialRow = { id: string; school_id: string; lesson_id: string; file_id: string; filename: string; extracted_text: string | null; created_at: Date };
const toMaterial = (r: MaterialRow) => new LessonMaterial(r.id, r.school_id, r.lesson_id, r.file_id, r.filename, r.extracted_text, r.created_at);
