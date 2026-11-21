import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";

/**
 * Used by UploadMaterial (and the extraction step). A file the teacher attached to a lesson ("Clase12-Indices.pdf"). The bytes live in `file`; this is the pointer plus
 * the text extracted from it (the source for the quiz generator). Pure: no I/O.
 */
export class LessonMaterial {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly lessonId: string,
    readonly fileId: string,
    readonly filename: string,
    private _extractedText: string | null,
    readonly createdAt: Date,
  ) {}

  /** TODO(contributor): which content types are accepted (PDF only?) and the filename rules. Only the non-empty filename is checked here. */
  static attach(p: { schoolId: string; lessonId: string; fileId: string; filename: string; now: Date }): LessonMaterial {
    const filename = p.filename.trim();
    if (filename.length === 0) throw new AppError(ErrorCode.MATERIAL_INVALID);
    return new LessonMaterial(newId(), p.schoolId, p.lessonId, p.fileId, filename, null, p.now);
  }

  get extractedText() { return this._extractedText; }

  /** The quiz generator needs text: a material without it is not a source yet (QUIZ_NO_MATERIAL when none has it). */
  get hasText(): boolean {
    return this._extractedText !== null && this._extractedText.trim().length > 0;
  }

  /** Called by the extraction step. Empty text is refused: an extraction that produced nothing is not a result. */
  setExtractedText(text: string): void {
    const t = text.trim();
    if (t.length === 0) throw new AppError(ErrorCode.MATERIAL_INVALID);
    this._extractedText = t;
  }
}
