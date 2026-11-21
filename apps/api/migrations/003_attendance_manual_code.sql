-- Typable fallback code for attendance (see attendance_round in the DBML).
ALTER TABLE attendance_round ADD COLUMN manual_step_seconds int NOT NULL DEFAULT 30;
