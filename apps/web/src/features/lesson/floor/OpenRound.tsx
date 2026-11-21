import type { Timestamp } from "@bufbuild/protobuf/wkt";
import { floor } from "@aula/rpc";
import { Banner, Button, Card, Countdown, Eyebrow, Heading, Progress, QueueRow, Text } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useCloseFloorRound, useOpenFloorToAll, useResolveTurn } from "../../../rpc/gen/hooks";
import { useCountdown } from "../../../rpc/time";
import { formatOffset, TURN_SECONDS } from "./format";

/** The open floor round (Docente-C-Palabra): the question, the responder with a countdown, and the waiting line. */
export function OpenRound({ round, serverTime, onFinished }: {
  round: floor.FloorRound;
  serverTime: Timestamp | undefined;
  /** The round is over (CORRECT or closed by the teacher): the parent shows the outcome. */
  onFinished: (roundId: string, correct: boolean) => void;
}) {
  const current = round.current;
  const deadline = current?.deadlineAt;
  const seconds = useCountdown(deadline, serverTime);

  const resolve = useResolveTurn({
    onSuccess: (res, vars) => {
      if (res.round?.status === floor.FloorRoundStatus.CLOSED) onFinished(round.id, vars.verdict === floor.TurnVerdict.CORRECT);
    },
  });
  const close = useCloseFloorRound({ onSuccess: () => onFinished(round.id, false) });
  const openAll = useOpenFloorToAll();

  const busy = resolve.isPending || close.isPending || openAll.isPending;
  const failure = resolve.error ?? close.error ?? openAll.error;
  const progress = Math.max(0, Math.min(100, (seconds / TURN_SECONDS) * 100));
  // The responder is position 1 in the line (floor.proto), the waiting students follow. The responder has no
  // offset in the contract, so their row says "Responde" instead of "+x,x s".
  const raised = round.queue.length + (current ? 1 : 0);
  const rows = [
    ...(current ? [{ key: "responder", name: current.name, offset: "Responde", current: true }] : []),
    ...round.queue.map((entry) => ({ key: entry.studentId, name: entry.name, offset: formatOffset(entry.offsetMs), current: false })),
  ];

  return (
    <div className="flex min-h-0 flex-1 gap-10">
      <div className="flex min-w-0 flex-1 flex-col gap-6">
        <div className="flex flex-col gap-2.5">
          <Eyebrow>Pregunta</Eyebrow>
          <Heading size={38}>{round.prompt}</Heading>
        </div>

        <Card padding={28} className="flex flex-col gap-[18px]">
          <div className="flex items-baseline justify-between gap-4">
            <Eyebrow>Responde ahora</Eyebrow>
            {current && deadline ? <Countdown seconds={seconds} size={40} /> : null}
            {current && !deadline ? <Text size={16} tone="strong">Ya respondió, falta tu veredicto</Text> : null}
          </div>
          {current ? (
            <Heading as="span" size={56}>{current.name}</Heading>
          ) : (
            <Text size={18} tone="strong">Esperando a que alguien levante la mano</Text>
          )}
          {current && deadline ? <Progress value={progress} label={`Tiempo de ${current.name}`} /> : null}

          {failure ? <Banner tone="danger">{errorMessage(failure)}</Banner> : null}
          <div className="mt-1.5 flex gap-3">
            <Button
              variant="accent" size="56"
              disabled={!current || busy}
              onClick={() => resolve.mutate({ roundId: round.id, verdict: floor.TurnVerdict.CORRECT })}
            >
              Acertó
            </Button>
            <Button
              variant="ghost" size="56"
              disabled={!current || busy}
              onClick={() => resolve.mutate({ roundId: round.id, verdict: floor.TurnVerdict.INCORRECT })}
            >
              No acertó · siguiente
            </Button>
          </div>
        </Card>
      </div>

      <div className="flex w-[420px] shrink-0 flex-col gap-3">
        <Eyebrow>{`Fila · ${raised} ${raised === 1 ? "pidió" : "pidieron"} la palabra`}</Eyebrow>
        {rows.length === 0 ? (
          <Text as="p" size={16}>Nadie ha levantado la mano.</Text>
        ) : (
          <ul className="m-0 flex flex-col gap-2 p-0">
            {rows.map((row, i) => (
              <QueueRow key={row.key} rank={i + 1} name={row.name} offset={row.offset} current={row.current} />
            ))}
          </ul>
        )}

        <div className="mt-auto flex flex-col items-stretch gap-2">
          <Button
            variant="ghost" size="52" block
            disabled={busy || round.openToAll}
            onClick={() => openAll.mutate({ roundId: round.id })}
          >
            {round.openToAll ? "Abierta a todos" : "Abrir a todos"}
          </Button>
          <Button variant="link" className="self-center" disabled={busy} onClick={() => close.mutate({ roundId: round.id })}>
            Cerrar ronda
          </Button>
        </div>
      </div>
    </div>
  );
}
