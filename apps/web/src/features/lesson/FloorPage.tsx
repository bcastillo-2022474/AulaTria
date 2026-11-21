import { useState } from "react";
import { useParams } from "react-router";
import { floor } from "@aula/rpc";
import { AppBar, Banner, Button, Content, Screen, Text } from "../../design-system";
import { errorMessage } from "../../rpc/hooks";
import { useGetFloorRound } from "../../rpc/gen/hooks";
import { useLessonLive } from "./useLessonLive";
import { OpenRound } from "./floor/OpenRound";
import { OpenRoundForm } from "./floor/OpenRoundForm";
import { RoundClosed } from "./floor/RoundClosed";

/**
 * Docente-C-Palabra: "Toma de la palabra" for the teacher. Three states:
 * no round open (ask the question), an open round (responder, countdown, queue), a finished round (outcome).
 */
export function FloorPage() {
  const { lessonId = "" } = useParams();
  const { snapshot, query } = useLessonLive(lessonId);
  // The round this screen has just finished (CORRECT or closed), and whether the teacher asked for a new question.
  const [closed, setClosed] = useState<{ roundId: string; correct: boolean } | null>(null);
  const [asking, setAsking] = useState(false);

  const activeId = snapshot?.activeFloorRound?.id;
  // The snapshot can lag a verdict by a moment: a round we just finished is over even while the snapshot still lists it.
  const over = closed !== null && (activeId === undefined || activeId === closed.roundId);
  const roundQuery = useGetFloorRound({ roundId: activeId ?? "" }, { enabled: !!activeId && !over });
  const serverClosed = !!activeId && !over && roundQuery.data?.round?.status === floor.FloorRoundStatus.CLOSED;
  const finished = over || serverClosed;

  const back = snapshot ? `${snapshot.courseName} · Clase ${snapshot.number}` : undefined;
  let body;
  if (!snapshot) {
    body = query.isError ? (
      <RetryBanner message={errorMessage(query.error)} onRetry={() => void query.refetch()} />
    ) : (
      <Text as="p" size={16}>Cargando la clase…</Text>
    );
  } else if (finished && !asking) {
    body = (
      <RoundClosed lessonId={lessonId} correct={closed?.correct ?? false} onNewQuestion={() => setAsking(true)} />
    );
  } else if (!finished && activeId) {
    body = roundQuery.data?.round ? (
      <OpenRound
        round={roundQuery.data.round}
        serverTime={snapshot.serverTime}
        onFinished={(roundId, correct) => setClosed({ roundId, correct })}
      />
    ) : roundQuery.isError ? (
      <RetryBanner message={errorMessage(roundQuery.error)} onRetry={() => void roundQuery.refetch()} />
    ) : (
      <Text as="p" size={16}>Cargando la ronda…</Text>
    );
  } else {
    body = (
      <OpenRoundForm
        lessonId={lessonId}
        onOpened={() => {
          setClosed(null);
          setAsking(false);
        }}
      />
    );
  }

  return (
    <Screen>
      <AppBar left={back} right={<span>Toma de la palabra</span>} />
      <Content>{body}</Content>
    </Screen>
  );
}

function RetryBanner({ message, onRetry }: { message: string; onRetry: () => void }) {
  return (
    <div className="flex flex-col items-start gap-4">
      <Banner tone="danger">{message}</Banner>
      <Button variant="ghost" size="48" onClick={onRetry}>Reintentar</Button>
    </div>
  );
}
