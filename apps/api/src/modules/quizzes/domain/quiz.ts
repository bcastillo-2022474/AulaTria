import { AppError, ErrorCode } from "../../../shared/errors.js";
import { newId } from "../../../shared/ids.js";
import { QuizAnswer } from "./quizAnswer.js";

export type QuizStatus = "draft" | "live" | "closed";
export type QuizOption = { label: string; text: string };
/** What the generator (LLM) or a teacher's edit produces: the whole question, written at once. */
export type QuizContent = { question: string; options: QuizOption[]; correctLabel: string; rationale: string | null };

export const QUIZ_LABELS = ["A", "B", "C", "D"] as const;
const DEFAULT_TIME_SECONDS = 45;
const MIN_TIME_SECONDS = 10;
const MAX_TIME_SECONDS = 300;

/**
 * Used by DraftQuiz, RegenerateQuiz, UpdateQuizTime, LaunchQuiz, CloseQuiz and AnswerQuiz (via acceptAnswer). One question. Pure: no I/O.
 *
 *   draft --launch--> live --close--> closed
 *
 * options is written whole, never patched (DBML). "At most one live quiz per lesson" is checked by the command
 * (QuizRepository.findLiveInLesson) because it spans rows.
 */
export class Quiz {
  constructor(
    readonly id: string,
    readonly schoolId: string,
    readonly lessonId: string,
    readonly createdById: string,
    readonly materialId: string | null,
    readonly slideNumber: number | null,
    private _content: QuizContent,
    private _timeLimitSeconds: number,
    private _status: QuizStatus,
    readonly createdAt: Date,
    private _launchedAt: Date | null,
    private _closesAt: Date | null,
    private _closedAt: Date | null,
  ) {}

  /** A new draft from generated content (DraftQuiz). */
  static draft(p: { schoolId: string; lessonId: string; createdById: string; materialId: string | null; slideNumber: number | null; content: QuizContent; now: Date }): Quiz {
    validateContent(p.content);
    return new Quiz(newId(), p.schoolId, p.lessonId, p.createdById, p.materialId, p.slideNumber, p.content, DEFAULT_TIME_SECONDS, "draft", p.now, null, null, null);
  }

  get content() { return this._content; }
  get options() { return this._content.options; }
  get correctLabel() { return this._content.correctLabel; }
  get timeLimitSeconds() { return this._timeLimitSeconds; }
  get status() { return this._status; }
  get launchedAt() { return this._launchedAt; }
  get closesAt() { return this._closesAt; }
  get closedAt() { return this._closedAt; }

  /** RegenerateQuiz: replaces the question while it is a draft. */
  regenerate(content: QuizContent): void {
    this.assertDraft();
    validateContent(content);
    this._content = content;
  }

  /**
   * UpdateQuizTime. Allowed on a draft and on a live quiz; never on a closed one (QUIZ_CLOSED).
   * Decision: while LIVE the clock moves with the new limit: closes_at = launched_at + the new limit, but never
   * before now. Lowering the limit below the time already spent closes the quiz at once (answers are refused
   * from that moment on); the teacher still closes it with CloseQuiz.
   */
  changeTime(seconds: number, now: Date): void {
    if (this._status === "closed") throw new AppError(ErrorCode.QUIZ_CLOSED);
    if (!Number.isInteger(seconds) || seconds < MIN_TIME_SECONDS || seconds > MAX_TIME_SECONDS) throw new AppError(ErrorCode.MALFORMED_ARGUMENT);
    this._timeLimitSeconds = seconds;
    if (this._status === "live" && this._launchedAt) {
      this._closesAt = new Date(Math.max(this._launchedAt.getTime() + seconds * 1000, now.getTime()));
    }
  }

  /** "Lanzar quiz": draft -> live. The closing time is now + the time limit. */
  launch(now: Date): void {
    this.assertDraft();
    this._status = "live";
    this._launchedAt = now;
    this._closesAt = new Date(now.getTime() + this._timeLimitSeconds * 1000);
  }

  /** "Mostrar resultado": live -> closed. A draft cannot be closed (QUIZ_NOT_LIVE). The server never closes by itself. */
  close(now: Date): void {
    this.assertLive();
    this._status = "closed";
    this._closedAt = now;
  }

  /**
   * First answer wins: this entity only decides whether the answer may be taken. The insert itself is
   * QuizRepository.insertAnswer, which refuses a second answer with QUIZ_ALREADY_ANSWERED.
   * A draft is not answerable (QUIZ_NOT_LIVE). At or after closes_at the answer is late (QUIZ_CLOSED).
   */
  acceptAnswer(p: { studentId: string; label: string; now: Date }): QuizAnswer {
    this.assertLive();
    if (this._closesAt && p.now.getTime() >= this._closesAt.getTime()) throw new AppError(ErrorCode.QUIZ_CLOSED);
    if (!this._content.options.some((o) => o.label === p.label)) throw new AppError(ErrorCode.MALFORMED_ARGUMENT);
    return QuizAnswer.record({ schoolId: this.schoolId, quizId: this.id, studentId: p.studentId, label: p.label, now: p.now });
  }

  /** Public so a command can refuse before an expensive step (the AI call) when the quiz can no longer change. */
  assertDraft(): void {
    if (this._status !== "draft") throw new AppError(ErrorCode.QUIZ_NOT_DRAFT);
  }

  private assertLive(): void {
    if (this._status === "closed") throw new AppError(ErrorCode.QUIZ_CLOSED);
    if (this._status !== "live") throw new AppError(ErrorCode.QUIZ_NOT_LIVE);
  }
}

/** Shape rules for a question: 4 options labelled A..D in order, non-empty texts, a correct label among them. */
function validateContent(c: QuizContent): void {
  const ok =
    c.question.trim().length > 0 &&
    c.options.length === QUIZ_LABELS.length &&
    c.options.every((o, i) => o.label === QUIZ_LABELS[i] && o.text.trim().length > 0) &&
    (QUIZ_LABELS as readonly string[]).includes(c.correctLabel);
  if (!ok) throw new AppError(ErrorCode.MALFORMED_ARGUMENT);
}
