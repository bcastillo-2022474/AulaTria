-- A round may open without the teacher's location (OpenAttendanceRoundRequest.has_location = false): it is then judged
-- by code only and every scan lands as needs_review / no_location. See attendance_round in the DBML.
-- Additive and reversible: existing rows already have both values, so no backfill.
ALTER TABLE attendance_round ALTER COLUMN room_lat DROP NOT NULL;
ALTER TABLE attendance_round ALTER COLUMN room_lng DROP NOT NULL;
-- A half-known room is meaningless: both or neither (DBML cannot express this check).
ALTER TABLE attendance_round ADD CONSTRAINT attendance_round_room_both_or_none CHECK ((room_lat IS NULL) = (room_lng IS NULL));
