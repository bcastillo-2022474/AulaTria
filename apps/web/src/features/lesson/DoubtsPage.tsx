import { useNavigate, useParams } from "react-router";
import { doubts } from "@aula/rpc";
import { AppBar, Banner, Button, Content, Screen, Text } from "../../design-system";
import { errorMessage } from "../../rpc/hooks";
import { useGetDoubtRound } from "../../rpc/gen/hooks";
import { DoubtsOpen } from "./doubts/DoubtsOpen";
import { DoubtsSummary } from "./doubts/DoubtsSummary";
import { lessonLabel } from "./format";
import { useLessonLive } from "./useLessonLive";

/**
 * Docente-C-DudasAbierta and Docente-C-DudasResumen on one route: the round's status decides which one. The round is the
 * cached GetDoubtRound (polled; the lesson stream refreshes it at once), so the screen turns into the summary by itself
 * when the teacher closes the round or the timer runs out.
 */
export function DoubtsPage() {
  const { lessonId = "", roundId = "" } = useParams();
  const navigate = useNavigate();
  const { snapshot } = useLessonLive(lessonId);
  const query = useGetDoubtRound({ roundId });
  const round = query.data?.round;
  const open = round?.status === doubts.DoubtRoundStatus.OPEN;

  let body;
  if (!round) {
    body = query.isError ? (
      <div className="flex flex-col items-start gap-4">
        <Banner tone="danger">{errorMessage(query.error)}</Banner>
        <Button variant="ghost" size="48" onClick={() => void query.refetch()}>Reintentar</Button>
        <Button href={`/lessons/${lessonId}/live`} variant="link">Volver a la clase</Button>
      </div>
    ) : (
      <Text as="p" size={16}>Cargando la ronda…</Text>
    );
  } else if (open) {
    body = <DoubtsOpen round={round} serverTime={snapshot?.serverTime} />;
  } else {
    body = <DoubtsSummary round={round} lessonId={lessonId} onNewRound={(id) => navigate(`/lessons/${lessonId}/doubts/${id}`)} />;
  }

  const right = !round ? "Ronda de dudas" : open ? "Ronda abierta" : `Ronda cerrada · ${round.submittedCount} de ${round.enrolledCount} respondieron`;
  return (
    <Screen>
      <AppBar left={lessonLabel(snapshot)} right={<span>{right}</span>} />
      <Content>{body}</Content>
    </Screen>
  );
}
