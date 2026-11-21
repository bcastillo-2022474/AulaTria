import { useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import type { Timestamp } from "@bufbuild/protobuf/wkt";
import { quizzes } from "@aula/rpc";
import { useGetQuiz } from "../../rpc/gen/hooks";
import { QuizDraft } from "./quiz/QuizDraft";
import { QuizLive } from "./quiz/QuizLive";
import { QuizResults } from "./quiz/QuizResults";
import { QuizLoading, QuizShell } from "./quiz/QuizShell";
import { QuizStart } from "./quiz/QuizStart";
import { useLessonLive } from "./useLessonLive";

/** Flow 4, "Quiz en vivo". Serves /lessons/:lessonId/quiz (start) and /quiz/:quizId (draft, live or results by status). */
export function QuizPage() {
  const { lessonId = "", quizId } = useParams();
  const navigate = useNavigate();
  const { snapshot } = useLessonLive(lessonId);
  const courseLabel = snapshot ? `${snapshot.courseName.toUpperCase()} · CLASE ${snapshot.number}` : undefined;

  // without an id: resume the quiz already running in the lesson, if any
  const activeQuizId = snapshot?.activeQuiz?.id;
  useEffect(() => {
    if (!quizId && activeQuizId) navigate(`/lessons/${lessonId}/quiz/${activeQuizId}`, { replace: true });
  }, [quizId, activeQuizId, lessonId, navigate]);

  if (!quizId) {
    if (!snapshot || activeQuizId) {
      return (
        <QuizShell courseLabel={courseLabel} status="Quiz">
          <QuizLoading error={null} onRetry={() => undefined} label="Cargando la clase…" />
        </QuizShell>
      );
    }
    return <QuizStart lessonId={lessonId} courseLabel={courseLabel} />;
  }
  return <QuizById quizId={quizId} courseLabel={courseLabel} serverTime={snapshot?.serverTime} />;
}

function QuizById({ quizId, courseLabel, serverTime }: { quizId: string; courseLabel?: string; serverTime?: Timestamp }) {
  const query = useGetQuiz({ quizId });
  const quiz = query.data?.quiz;
  if (!quiz) {
    return (
      <QuizShell courseLabel={courseLabel} status="Quiz">
        <QuizLoading error={query.error} onRetry={() => void query.refetch()} label="Cargando el quiz…" />
      </QuizShell>
    );
  }
  switch (quiz.status) {
    case quizzes.QuizStatus.LIVE:
      return <QuizLive quiz={quiz} serverTime={serverTime} courseLabel={courseLabel} />;
    case quizzes.QuizStatus.CLOSED:
      return <QuizResults quiz={quiz} courseLabel={courseLabel} />;
    default:
      return <QuizDraft quiz={quiz} courseLabel={courseLabel} />;
  }
}
