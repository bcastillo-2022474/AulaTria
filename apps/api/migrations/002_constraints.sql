-- Constraints DBML cannot express. (RLS and a separate runtime role were considered and
-- deliberately left out for the MVP: tenancy is enforced by the tenant id in every query
-- signature, school_id on every table and the composite foreign keys in 001.)

-- A course has at most one lesson that is in the lobby or live.
CREATE UNIQUE INDEX one_active_lesson_per_course ON lesson (course_id) WHERE status IN ('lobby', 'live');
-- At most one open round of each kind per lesson.
CREATE UNIQUE INDEX one_open_doubt_round ON doubt_round (lesson_id) WHERE status = 'open';
CREATE UNIQUE INDEX one_open_floor_round ON floor_round (lesson_id) WHERE status = 'open';
CREATE UNIQUE INDEX one_open_attendance_round ON attendance_round (lesson_id) WHERE status = 'open';
CREATE UNIQUE INDEX one_live_quiz ON quiz (lesson_id) WHERE status = 'live';
-- At most one student at a time is answering the floor.
CREATE UNIQUE INDEX one_answering_turn ON floor_turn (round_id) WHERE state = 'answering';
-- Credential rule: only invited users may lack a password.
ALTER TABLE app_user ADD CONSTRAINT credential_or_invited CHECK (status = 'invited' OR password_hash IS NOT NULL);
