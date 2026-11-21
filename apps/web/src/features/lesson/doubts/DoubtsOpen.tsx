import type { Timestamp } from "@bufbuild/protobuf/wkt";
import { timestampDate } from "@bufbuild/protobuf/wkt";
import type { doubts } from "@aula/rpc";
import { Banner, Button, Card, Countdown, Eyebrow, Meter, Progress, Stat, Text } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useCloseDoubtRound, useExtendDoubtRound } from "../../../rpc/gen/hooks";
import { useCountdown } from "../../../rpc/time";

/** Docente-C-DudasAbierta: the countdown to `closes_at`, how many students sent a doubt, "+30 s" and "Cerrar ahora". */
export function DoubtsOpen({ round, serverTime }: { round: doubts.DoubtRound; serverTime: Timestamp | undefined }) {
  const seconds = useCountdown(round.closesAt, serverTime);
  const extend = useExtendDoubtRound();
  const close = useCloseDoubtRound();
  const busy = extend.isPending || close.isPending;
  const failure = extend.error ?? close.error;

  // The bar is "time left / total": the total grows with every +30 s (opened_at .. closes_at).
  const total = round.openedAt && round.closesAt ? (timestampDate(round.closesAt).getTime() - timestampDate(round.openedAt).getTime()) / 1000 : 0;
  const progress = total > 0 ? Math.max(0, Math.min(100, (seconds / total) * 100)) : 0;
  const share = round.enrolledCount > 0 ? (round.submittedCount / round.enrolledCount) * 100 : 0;

  return (
    <div className="flex min-h-0 flex-1 gap-10">
      <div className="flex min-w-0 flex-1 flex-col gap-6">
        <Eyebrow>Ronda de dudas · abierta</Eyebrow>
        <Countdown seconds={seconds} size={200} />
        <Progress value={progress} label="Tiempo que queda de la ronda" />
        <Text as="p" size={18}>Tus estudiantes están escribiendo su duda.</Text>
        {failure ? <Banner tone="danger">{errorMessage(failure)}</Banner> : null}
        <div className="flex gap-3">
          <Button variant="ghost" size="56" disabled={busy} onClick={() => extend.mutate({ roundId: round.id, seconds: 30 })}>+30 s</Button>
          <Button size="56" disabled={busy} onClick={() => close.mutate({ roundId: round.id })}>
            {close.isPending ? "Cerrando…" : "Cerrar ahora"}
          </Button>
        </div>
      </div>
      <div className="flex w-[430px] shrink-0 flex-col justify-center">
        <Card padding={28} className="flex flex-col gap-[18px]">
          <Stat value={String(round.submittedCount)} size={72} caption={`de ${round.enrolledCount} enviaron`} />
          <Meter value={share} lost={0} label="Estudiantes que enviaron su duda" />
        </Card>
      </div>
    </div>
  );
}
