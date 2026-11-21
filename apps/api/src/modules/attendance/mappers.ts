import { attendance } from "@aula/rpc";
import type { AttendanceStatus } from "./domain/attendanceRecord.js";
import type { ReviewReason } from "./domain/attendanceRound.js";

/** Shared by CheckIn (the answer to the student) and GetAttendanceRound (the teacher's review list). Mapped explicitly. */
export const statusToProto = (s: AttendanceStatus): attendance.AttendanceStatus =>
  s === "present" ? attendance.AttendanceStatus.PRESENT : s === "needs_review" ? attendance.AttendanceStatus.NEEDS_REVIEW : attendance.AttendanceStatus.ABSENT;

export const reasonToProto = (r: ReviewReason | null): attendance.ReviewReason =>
  r === "too_far" ? attendance.ReviewReason.TOO_FAR
    : r === "no_location" ? attendance.ReviewReason.NO_LOCATION
      : r === "low_accuracy" ? attendance.ReviewReason.LOW_ACCURACY
        : attendance.ReviewReason.UNSPECIFIED;
