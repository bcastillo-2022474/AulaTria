import type { attendance } from "@aula/rpc";
import { Button, Eyebrow, Meter, Stat, Text } from "../../../design-system";
import { ReviewList, noRecordCount, pendingCount } from "./ReviewList";

/** Docente-C-AsistCerrada: the final count and the review list ("Aceptar" / "Ausente"), then back to the class. */
export function AttendanceClosed({ round, lessonId }: { round: attendance.AttendanceRound; lessonId: string }) {
  const pending = pendingCount(round);
  const share = round.enrolledCount > 0 ? (round.presentCount / round.enrolledCount) * 100 : 0;
  const reviewShare = round.enrolledCount > 0 ? (pending / round.enrolledCount) * 100 : 0;
  return (
    <div className="flex min-h-0 flex-1 gap-14">
      <div className="flex w-[420px] shrink-0 flex-col justify-center gap-5">
        <Eyebrow>Asistencia cerrada</Eyebrow>
        <Stat value={String(round.presentCount)} size={130} caption="presentes" captionSize={22} />
        <Meter value={share} lost={reviewShare} label="Presentes y por revisar" />
        <Text size={15}>{pending} por revisar · {noRecordCount(round)} ausentes sin registro</Text>
      </div>
      <div className="flex min-h-0 min-w-0 flex-1 flex-col justify-center gap-3.5">
        <Eyebrow>Por revisar</Eyebrow>
        <div className="flex min-h-0 flex-col gap-3.5 overflow-y-auto"><ReviewList round={round} /></div>
        <div className="mt-5"><Button href={`/lessons/${lessonId}/live`} size="56" block>Volver a la clase</Button></div>
      </div>
    </div>
  );
}
