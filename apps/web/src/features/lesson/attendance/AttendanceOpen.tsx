import { useEffect, useRef } from "react";
import { useSearchParams } from "react-router";
import type { Timestamp } from "@bufbuild/protobuf/wkt";
import type { attendance } from "@aula/rpc";
import { Banner, Button, CodeBox, Eyebrow, Heading, Meter, QrFrame, Stat, Text } from "../../../design-system";
import { errorMessage } from "../../../rpc/hooks";
import { useCloseAttendanceRound, useGetAttendanceCodes } from "../../../rpc/gen/hooks";
import { useCountdown } from "../../../rpc/time";
import { ReviewList, pendingCount } from "./ReviewList";
import { qrCells } from "./qr";

/**
 * Docente-C-Asistencia: the rotating QR (8 s) and the typable code, the live counters and "Cerrar asistencia". The counters
 * come from GetAttendanceRound, which the lesson stream refreshes the moment anyone checks in (no manual refresh). "Revisar
 * los N" swaps the QR for the review list without closing the round.
 */
export function AttendanceOpen({ round, serverTime, withoutLocation }: { round: attendance.AttendanceRound; serverTime: Timestamp | undefined; withoutLocation: boolean }) {
  const [params, setParams] = useSearchParams();
  const reviewing = params.get("review") === "1";
  const close = useCloseAttendanceRound();

  const codes = useGetAttendanceCodes({ roundId: round.id });
  const left = useCountdown(codes.data?.qrExpiresAt, serverTime);
  const expiry = Number(codes.data?.qrExpiresAt?.seconds ?? 0n);
  const refetchedFor = useRef(0);
  const { refetch } = codes;
  // The QR changes every 8 s: fetch the next one the instant this one ends (the poll is only the fallback).
  useEffect(() => {
    if (left === 0 && expiry && refetchedFor.current !== expiry) {
      refetchedFor.current = expiry;
      void refetch();
    }
  }, [left, expiry, refetch]);

  const pending = pendingCount(round);
  const present = round.presentCount;
  const share = round.enrolledCount > 0 ? (present / round.enrolledCount) * 100 : 0;
  const reviewShare = round.enrolledCount > 0 ? (pending / round.enrolledCount) * 100 : 0;

  return (
    <div className="flex min-h-0 flex-1 items-center gap-14">
      <div className="flex min-w-0 flex-1 flex-col gap-[26px]">
        <Heading size={58} className="leading-[1.05] text-balance">Escanea para marcar tu asistencia</Heading>
        <Stat value={String(present)} size={110} caption={`de ${round.enrolledCount} presentes`} captionSize={24} accent />
        <Meter value={share} lost={reviewShare} label="Presentes y por revisar" />
        <div className="flex gap-6 text-body-14 text-stage-ink">
          <span>{round.inRoomCount} en el aula</span>
          <span>{pending} por revisar</span>
        </div>
        {withoutLocation ? <Banner tone="warn">Abriste la asistencia sin ubicación: cada registro quedará por revisar.</Banner> : null}
        {close.error ? <Banner tone="danger">{errorMessage(close.error)}</Banner> : null}
        <div className="flex gap-3">
          <Button size="56" disabled={close.isPending} onClick={() => close.mutate({ roundId: round.id })}>{close.isPending ? "Cerrando…" : "Cerrar asistencia"}</Button>
          {pending > 0 || reviewing ? (
            <Button variant="ghost" size="56" onClick={() => setParams(reviewing ? {} : { review: "1" })}>{reviewing ? "Ver el código" : `Revisar los ${pending}`}</Button>
          ) : null}
        </div>
      </div>

      {reviewing ? (
        <div className="flex w-[480px] shrink-0 flex-col gap-3.5 self-stretch overflow-y-auto">
          <Eyebrow>Por revisar</Eyebrow>
          <ReviewList round={round} />
        </div>
      ) : (
        <div className="flex shrink-0 flex-col items-center gap-3.5">
          {codes.data ? (
            <>
              <QrFrame large cells={qrCells(codes.data.qrCode)} label="Código QR de asistencia" />
              <Text size={14}>El código cambia en {left} s</Text>
              <Eyebrow>¿Sin cámara? Escribe este código</Eyebrow>
              <CodeBox code={codes.data.manualCode} />
            </>
          ) : codes.isError ? (
            <div className="flex flex-col items-start gap-3">
              <Banner tone="danger">{errorMessage(codes.error)}</Banner>
              <Button variant="ghost" size="48" onClick={() => void codes.refetch()}>Reintentar</Button>
            </div>
          ) : (
            <Text size={16}>Generando el código…</Text>
          )}
        </div>
      )}
    </div>
  );
}
