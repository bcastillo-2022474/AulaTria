/** Used by AnswerQuiz. A student's answer to one quiz. Identified by (quiz_id, student_id): one answer per student per quiz. Pure. */
export class QuizAnswer {
  constructor(
    readonly schoolId: string,
    readonly quizId: string,
    readonly studentId: string,
    readonly label: string,
    readonly answeredAt: Date,
  ) {}

  static record(p: { schoolId: string; quizId: string; studentId: string; label: string; now: Date }): QuizAnswer {
    return new QuizAnswer(p.schoolId, p.quizId, p.studentId, p.label, p.now);
  }
}
