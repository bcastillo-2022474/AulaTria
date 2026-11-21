import { useParams } from "react-router";
import { lessons } from "@aula/rpc";
import { AppBar, Banner, Button, Card, Content, Eyebrow, Heading, PulseChart, Screen, Text, VideoPanel } from "../../design-system";
import { SignOutButton } from "../auth/SignOutButton";
import { errorMessage, useAulaQuery } from "../../rpc/hooks";
import { useSession } from "../../rpc/session";
import { clock, clockSeconds } from "./format";

/** Docente-C-Pulso: the class after the fact. Pulse bars per minute, quiz/doubt markers, the peak moment, attendance. */
export function ReviewPage() {
  const { lessonId = "" } = useParams();
  const session = useSession();
  const review = useAulaQuery(lessons.LessonsService.method.getLessonReview, { lessonId });
  const r = review.data;

  const minutes = r?.pulseSeries.length ?? 0;
  const markers = (r?.markers ?? []).map((m) => ({ at: ((m.minute + 0.5) / Math.max(minutes, 1)) * 100, label: m.label }));
  const peak = r?.peak;

  return (
    <Screen>
      <AppBar
        left={r ? `${r.courseName.toUpperCase()} · CLASE ${r.number}` : undefined}
        right={
          <>
            {r ? <span>Duración {clockSeconds(r.durationSeconds)} ·</span> : null}
            {r ? <Button variant="link" href={`/courses/${r.courseId}/students`}>Ver historial de estudiantes →</Button> : null}
            <span>{session?.name}</span>
            <SignOutButton />
          </>
        }
      />
      <Content>
        {!r ? (
          review.isError ? (
            <div className="flex flex-col items-start gap-4">
              <Banner tone="danger">{errorMessage(review.error)}</Banner>
              <Button variant="ghost" size="48" onClick={() => void review.refetch()}>Reintentar</Button>
            </div>
          ) : (
            <Text as="p" size={16}>Cargando la clase…</Text>
          )
        ) : (
          <>
            <div className="flex h-[372px] gap-6">
              <VideoPanel
                caption="La grabación de la clase todavía no está disponible"
                timecode={peak ? `${clock(peak.minute)} / ${clockSeconds(r.durationSeconds)}` : clockSeconds(r.durationSeconds)}
              />
              <Card padding={26} className="flex w-[350px] flex-none flex-col gap-3.5">
                {peak ? (
                  <>
                    <Eyebrow>EN ESTE MOMENTO · {clock(peak.minute)}</Eyebrow>
                    <Heading as="h2" size={32} className="text-pretty">{peak.lostCount} de {r.enrolledCount} marcaron «me perdí»</Heading>
                    <Text as="p" size={15} className="leading-[1.45]">
                      {peak.slideNumber > 0 ? `Diapositiva ${peak.slideNumber}. ` : ""}
                      {peak.topDoubt ? `La duda más repetida: ${peak.topDoubt}` : "Nadie dejó dudas cerca de este momento."}
                    </Text>
                  </>
                ) : (
                  <>
                    <Eyebrow>EN ESTE MOMENTO</Eyebrow>
                    <Heading as="h2" size={32} className="text-pretty">Nadie marcó «me perdí»</Heading>
                    <Text as="p" size={15} className="leading-[1.45]">La clase no tuvo momentos de confusión registrados.</Text>
                  </>
                )}
                <div className="mt-auto flex flex-col gap-1">
                  <Eyebrow>ASISTENCIA</Eyebrow>
                  <Text size={15} tone="strong">
                    {r.attendance?.present ?? 0} presentes · {r.attendance?.needsReview ?? 0} por revisar · {r.attendance?.absent ?? 0} ausentes
                  </Text>
                </div>
              </Card>
            </div>
            {minutes > 0 ? (
              <PulseChart
                values={r.pulseSeries.map((p) => p.okPercent)}
                markers={markers}
                playhead={peak ? ((peak.minute + 0.5) / minutes) * 100 : undefined}
              />
            ) : (
              <Text as="p" size={16}>Esta clase no llegó a iniciar, así que no hay pulso que revisar.</Text>
            )}
          </>
        )}
      </Content>
    </Screen>
  );
}
