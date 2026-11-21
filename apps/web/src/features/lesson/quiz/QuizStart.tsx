import { useNavigate } from "react-router";
import { Banner, Button, Card, Heading, Text } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useDraftQuiz } from "../../../rpc/gen/hooks";
import { QuizShell } from "./QuizShell";

/** /lessons/:lessonId/quiz without a quiz yet: "Lanzar quiz" drafts one question from the class material. */
export function QuizStart({ lessonId, courseLabel }: { lessonId: string; courseLabel?: string }) {
  const navigate = useNavigate();
  const draft = useDraftQuiz({
    onSuccess: (res) => {
      const id = res.quiz?.id;
      if (id) navigate(`/lessons/${lessonId}/quiz/${id}`, { replace: true });
    },
  });
  return (
    <QuizShell courseLabel={courseLabel} status="Quiz">
      <div className="flex max-w-[560px] flex-col items-start gap-6">
        <Heading size={48}>Lanzar quiz</Heading>
        <Text as="p" size={18}>La pregunta se arma a partir del material de la clase. Podrás revisarla antes de lanzarla.</Text>
        <Card padding={22} className="flex w-full flex-col gap-4">
          <Button size="56" block onClick={() => draft.mutate({ lessonId })} disabled={draft.isPending}>
            {draft.isPending ? "Armando la pregunta…" : "Armar pregunta"}
          </Button>
          {draft.isPending ? <Text as="p" size={14}>Esto puede tardar unos segundos.</Text> : null}
          {draft.error ? <Banner tone="danger">{errorMessage(draft.error)}</Banner> : null}
        </Card>
        <Button href={`#/lessons/${lessonId}/live`} variant="link">‹ Volver a la clase</Button>
      </div>
    </QuizShell>
  );
}
