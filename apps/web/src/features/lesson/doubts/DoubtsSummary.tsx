import { useState } from "react";
import { doubts } from "@aula/rpc";
import { Banner, Button, Card, CountPill, Eyebrow, Heading, Progress, Text, type BrandColor } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useListDoubts, useOpenDoubtRound } from "../../../rpc/gen/hooks";
import { plural } from "../../courses/format";

const pillColors: readonly BrandColor[] = ["danger", "info", "sun"];

/**
 * Docente-C-DudasResumen: the round is over. The grouped topics only when the server says they are READY (never invented);
 * the doubts exactly as written (no names) are always one tap away, and open by themselves while there are no groups.
 */
export function DoubtsSummary({ round, lessonId, onNewRound }: {
  round: doubts.DoubtRound; lessonId: string; onNewRound: (roundId: string) => void;
}) {
  const ready = round.summaryStatus === doubts.SummaryStatus.READY && round.groups.length > 0;
  const [override, setOverride] = useState<boolean | null>(null);
  const showOriginals = override ?? !ready;
  const list = useListDoubts({ roundId: round.id }, { enabled: showOriginals });
  const another = useOpenDoubtRound({ onSuccess: (res) => res.round && onNewRound(res.round.id) });
  const biggest = Math.max(1, ...round.groups.map((g) => g.studentCount));

  const subtitle = ready
    ? `Agrupadas desde las ${round.submittedCount} respuestas.`
    : round.summaryStatus === doubts.SummaryStatus.FAILED
      ? "No pudimos agrupar las dudas. Aquí están tal como las escribieron."
      : "Las dudas todavía no están agrupadas. Aquí están tal como las escribieron.";

  return (
    <div className="flex min-h-0 flex-1 flex-col gap-6">
      <div className="flex flex-col gap-1.5">
        <Heading size={48}>{ready ? `${round.groups.length} dudas principales` : plural(round.submittedCount, "duda recibida", "dudas recibidas")}</Heading>
        <Text size={16}>{subtitle}</Text>
      </div>

      {ready ? (
        <div className="grid grid-cols-3 gap-5">
          {round.groups.map((g, i) => (
            <Card key={g.id} padding={26} className="flex flex-col gap-[18px]">
              <div className="flex items-center justify-between gap-3">
                <CountPill color={pillColors[i % pillColors.length]}>{plural(g.studentCount, "estudiante", "estudiantes")}</CountPill>
                {g.slideNumber > 0 ? <Text size={14}>Diap. {g.slideNumber}</Text> : null}
              </div>
              <Progress value={(g.studentCount / biggest) * 100} label="Estudiantes con esta duda" />
              <Heading as="h2" size={26}>{g.title}</Heading>
              {g.sampleQuote ? <Text size={14}>«{g.sampleQuote}»</Text> : null}
            </Card>
          ))}
        </div>
      ) : null}

      {showOriginals ? (
        <div className="flex flex-col gap-3">
          <Eyebrow>DUDAS ORIGINALES</Eyebrow>
          {list.isError ? (
            <Banner tone="danger">{errorMessage(list.error)}</Banner>
          ) : !list.data ? (
            <Text as="p" size={16}>Cargando las dudas…</Text>
          ) : list.data.doubts.length === 0 ? (
            <Card padding={22}><Text as="p" size={16}>Nadie envió una duda en esta ronda.</Text></Card>
          ) : (
            list.data.doubts.map((d) => (
              <Card key={d.id} padding={20}><Text as="p" size={17} tone="strong">{d.text}</Text></Card>
            ))
          )}
        </div>
      ) : null}

      {another.error ? <Banner tone="danger">{errorMessage(another.error)}</Banner> : null}
      <div className="mt-auto flex flex-wrap items-center justify-between gap-3">
        {ready ? (
          <Button variant="link" onClick={() => setOverride(!showOriginals)}>
            {showOriginals ? "Ocultar las dudas originales" : `Ver las ${round.submittedCount} dudas originales`}
          </Button>
        ) : <span />}
        <div className="flex gap-3">
          <Button variant="ghost" size="56" disabled={another.isPending} onClick={() => another.mutate({ lessonId, durationSeconds: 30 })}>
            Otra ronda · 30 s
          </Button>
          <Button href={`/lessons/${lessonId}/live`} size="56">Continuar clase</Button>
        </div>
      </div>
    </div>
  );
}
