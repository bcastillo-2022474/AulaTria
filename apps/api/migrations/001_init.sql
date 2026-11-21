-- SQL dump generated using DBML (dbml.dbdiagram.io)
-- Database: PostgreSQL
-- Generated at: 2026-11-21T17:46:47.611Z

CREATE TYPE "user_role" AS ENUM (
  'admin',
  'teacher',
  'student'
);

CREATE TYPE "user_status" AS ENUM (
  'invited',
  'active',
  'disabled'
);

CREATE TYPE "device_platform" AS ENUM (
  'ios',
  'android',
  'desktop',
  'web'
);

CREATE TYPE "TpStatus" AS ENUM (
  'active',
  'archived'
);

CREATE TYPE "file_status" AS ENUM (
  'pending',
  'uploaded'
);

CREATE TYPE "lesson_status" AS ENUM (
  'lobby',
  'live',
  'ended',
  'cancelled'
);

CREATE TYPE "pulse_state" AS ENUM (
  'ok',
  'lost'
);

CREATE TYPE "doubt_round_status" AS ENUM (
  'open',
  'closed'
);

CREATE TYPE "summary_status" AS ENUM (
  'pending',
  'ready',
  'failed'
);

CREATE TYPE "floor_round_status" AS ENUM (
  'open',
  'closed'
);

CREATE TYPE "floor_turn_state" AS ENUM (
  'queued',
  'answering',
  'answered',
  'correct',
  'incorrect',
  'withdrawn'
);

CREATE TYPE "quiz_status" AS ENUM (
  'draft',
  'live',
  'closed'
);

CREATE TYPE "attendance_round_status" AS ENUM (
  'open',
  'closed'
);

CREATE TYPE "attendance_status" AS ENUM (
  'present',
  'needs_review',
  'absent'
);

CREATE TYPE "attendance_review_reason" AS ENUM (
  'too_far',
  'no_location',
  'low_accuracy'
);

CREATE TABLE "school" (
  "id" uuid PRIMARY KEY NOT NULL,
  "name" varchar NOT NULL,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz NOT NULL
);

CREATE TABLE "app_user" (
  "id" uuid PRIMARY KEY NOT NULL,
  "school_id" uuid NOT NULL,
  "role" user_role NOT NULL,
  "status" user_status NOT NULL DEFAULT 'active',
  "name" varchar NOT NULL,
  "email" varchar UNIQUE NOT NULL,
  "password_hash" varchar,
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz NOT NULL
);

CREATE TABLE "invitation" (
  "id" uuid PRIMARY KEY NOT NULL,
  "school_id" uuid NOT NULL,
  "user_id" uuid NOT NULL,
  "created_by_id" uuid,
  "token_hash" varchar UNIQUE NOT NULL,
  "expires_at" timestamptz NOT NULL,
  "accepted_at" timestamptz,
  "revoked_at" timestamptz,
  "created_at" timestamptz NOT NULL
);

CREATE TABLE "refresh_token" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "user_id" uuid NOT NULL,
  "token_hash" varchar UNIQUE NOT NULL,
  "device_label" varchar,
  "replaced_by_id" uuid,
  "created_at" timestamptz NOT NULL,
  "expires_at" timestamptz NOT NULL,
  "revoked_at" timestamptz
);

CREATE TABLE "device" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "user_id" uuid NOT NULL,
  "platform" device_platform NOT NULL,
  "push_token" varchar UNIQUE NOT NULL,
  "updated_at" timestamptz NOT NULL
);

CREATE TABLE "course" (
  "id" uuid PRIMARY KEY NOT NULL,
  "school_id" uuid NOT NULL,
  "teacher_id" uuid NOT NULL,
  "name" varchar NOT NULL,
  "code" varchar,
  "join_code" varchar UNIQUE NOT NULL,
  "tp_status" "TpStatus" NOT NULL DEFAULT 'active',
  "created_at" timestamptz NOT NULL,
  "updated_at" timestamptz NOT NULL
);

CREATE TABLE "enrollment" (
  "school_id" uuid NOT NULL,
  "course_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "joined_at" timestamptz NOT NULL,
  PRIMARY KEY ("course_id", "student_id")
);

CREATE TABLE "file" (
  "id" uuid PRIMARY KEY NOT NULL,
  "school_id" uuid NOT NULL,
  "content_type" varchar NOT NULL,
  "size_bytes" bigint NOT NULL,
  "storage_key" varchar UNIQUE NOT NULL,
  "status" file_status NOT NULL DEFAULT 'pending',
  "created_at" timestamptz NOT NULL,
  "uploaded_at" timestamptz
);

CREATE TABLE "lesson" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "course_id" uuid NOT NULL,
  "number" int NOT NULL,
  "status" lesson_status NOT NULL DEFAULT 'lobby',
  "room_opened_at" timestamptz NOT NULL,
  "started_at" timestamptz,
  "ended_at" timestamptz,
  "recording_file_id" uuid
);

CREATE TABLE "lesson_participant" (
  "school_id" uuid NOT NULL,
  "lesson_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "joined_at" timestamptz NOT NULL,
  PRIMARY KEY ("lesson_id", "student_id")
);

CREATE TABLE "lesson_material" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "lesson_id" uuid NOT NULL,
  "file_id" uuid NOT NULL,
  "filename" varchar NOT NULL,
  "extracted_text" text,
  "created_at" timestamptz NOT NULL
);

CREATE TABLE "pulse_event" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "lesson_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "state" pulse_state NOT NULL,
  "created_at" timestamptz NOT NULL
);

CREATE TABLE "doubt_round" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "lesson_id" uuid NOT NULL,
  "status" doubt_round_status NOT NULL DEFAULT 'open',
  "summary_status" summary_status NOT NULL DEFAULT 'pending',
  "opened_at" timestamptz NOT NULL,
  "closes_at" timestamptz NOT NULL,
  "closed_at" timestamptz
);

CREATE TABLE "doubt_group" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "round_id" uuid NOT NULL,
  "rank" int NOT NULL,
  "title" varchar NOT NULL,
  "slide_number" int,
  "sample_quote" varchar
);

CREATE TABLE "doubt" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "round_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "group_id" uuid,
  "text" varchar NOT NULL,
  "created_at" timestamptz NOT NULL,
  "edited_at" timestamptz
);

CREATE TABLE "floor_round" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "lesson_id" uuid NOT NULL,
  "prompt" text NOT NULL,
  "status" floor_round_status NOT NULL DEFAULT 'open',
  "open_to_all" boolean NOT NULL DEFAULT false,
  "opened_at" timestamptz NOT NULL,
  "closed_at" timestamptz
);

CREATE TABLE "floor_turn" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "round_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "state" floor_turn_state NOT NULL DEFAULT 'queued',
  "raised_at" timestamptz NOT NULL,
  "answering_since" timestamptz,
  "answer_deadline_at" timestamptz,
  "resolved_at" timestamptz
);

CREATE TABLE "quiz" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "lesson_id" uuid NOT NULL,
  "created_by_id" uuid NOT NULL,
  "material_id" uuid,
  "slide_number" int,
  "question" text NOT NULL,
  "options" jsonb NOT NULL,
  "correct_label" char(1) NOT NULL,
  "rationale" text,
  "time_limit_seconds" int NOT NULL DEFAULT 45,
  "status" quiz_status NOT NULL DEFAULT 'draft',
  "created_at" timestamptz NOT NULL,
  "launched_at" timestamptz,
  "closes_at" timestamptz,
  "closed_at" timestamptz
);

CREATE TABLE "quiz_answer" (
  "school_id" uuid NOT NULL,
  "quiz_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "option_label" char(1) NOT NULL,
  "answered_at" timestamptz NOT NULL,
  PRIMARY KEY ("quiz_id", "student_id")
);

CREATE TABLE "attendance_round" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "lesson_id" uuid NOT NULL,
  "status" attendance_round_status NOT NULL DEFAULT 'open',
  "qr_secret" varchar NOT NULL,
  "qr_step_seconds" int NOT NULL DEFAULT 8,
  "room_lat" numeric(9,6) NOT NULL,
  "room_lng" numeric(9,6) NOT NULL,
  "radius_m" int NOT NULL DEFAULT 50,
  "opened_at" timestamptz NOT NULL,
  "closed_at" timestamptz
);

CREATE TABLE "attendance_record" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "round_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "status" attendance_status NOT NULL,
  "review_reason" attendance_review_reason,
  "scanned_at" timestamptz NOT NULL,
  "lat" numeric(9,6),
  "lng" numeric(9,6),
  "accuracy_m" int,
  "distance_m" int,
  "reviewed_by_id" uuid,
  "reviewed_at" timestamptz
);

CREATE TABLE "student_note" (
  "school_id" uuid NOT NULL,
  "id" uuid PRIMARY KEY NOT NULL,
  "course_id" uuid NOT NULL,
  "student_id" uuid NOT NULL,
  "body" text NOT NULL,
  "updated_at" timestamptz NOT NULL
);

CREATE UNIQUE INDEX ON "app_user" ("school_id", "id");

CREATE INDEX ON "app_user" ("school_id", "role");

CREATE UNIQUE INDEX ON "course" ("school_id", "id");

CREATE INDEX ON "course" ("teacher_id", "tp_status");

CREATE INDEX ON "enrollment" ("student_id");

CREATE UNIQUE INDEX ON "file" ("school_id", "id");

CREATE UNIQUE INDEX ON "lesson" ("school_id", "id");

CREATE UNIQUE INDEX ON "lesson" ("course_id", "number");

CREATE INDEX ON "lesson" ("course_id", "status");

CREATE UNIQUE INDEX ON "lesson_material" ("school_id", "id");

CREATE INDEX ON "pulse_event" ("lesson_id", "created_at");

CREATE INDEX ON "pulse_event" ("lesson_id", "student_id", "created_at");

CREATE UNIQUE INDEX ON "doubt_round" ("school_id", "id");

CREATE INDEX ON "doubt_round" ("lesson_id", "opened_at");

CREATE UNIQUE INDEX ON "doubt_group" ("school_id", "id");

CREATE UNIQUE INDEX ON "doubt_group" ("round_id", "rank");

CREATE UNIQUE INDEX ON "doubt" ("round_id", "student_id");

CREATE INDEX ON "doubt" ("group_id");

CREATE UNIQUE INDEX ON "floor_round" ("school_id", "id");

CREATE UNIQUE INDEX ON "floor_turn" ("round_id", "student_id");

CREATE INDEX ON "floor_turn" ("round_id", "state", "raised_at");

CREATE UNIQUE INDEX ON "quiz" ("school_id", "id");

CREATE INDEX ON "quiz" ("lesson_id", "created_at");

CREATE UNIQUE INDEX ON "attendance_round" ("school_id", "id");

CREATE UNIQUE INDEX ON "attendance_record" ("round_id", "student_id");

CREATE UNIQUE INDEX ON "student_note" ("course_id", "student_id");

ALTER TABLE "app_user" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "invitation" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "refresh_token" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "refresh_token" ADD FOREIGN KEY ("replaced_by_id") REFERENCES "refresh_token" ("id");

ALTER TABLE "device" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "course" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "enrollment" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "file" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "lesson" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "lesson_participant" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "lesson_material" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "pulse_event" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "doubt_round" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "doubt_group" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "doubt" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "floor_round" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "floor_turn" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "quiz" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "quiz_answer" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "attendance_round" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "attendance_record" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "student_note" ADD FOREIGN KEY ("school_id") REFERENCES "school" ("id");

ALTER TABLE "invitation" ADD FOREIGN KEY ("school_id", "user_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "invitation" ADD FOREIGN KEY ("school_id", "created_by_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "refresh_token" ADD FOREIGN KEY ("school_id", "user_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "device" ADD FOREIGN KEY ("school_id", "user_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "course" ADD FOREIGN KEY ("school_id", "teacher_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "enrollment" ADD FOREIGN KEY ("school_id", "course_id") REFERENCES "course" ("school_id", "id");

ALTER TABLE "enrollment" ADD FOREIGN KEY ("school_id", "student_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "lesson" ADD FOREIGN KEY ("school_id", "course_id") REFERENCES "course" ("school_id", "id");

ALTER TABLE "lesson" ADD FOREIGN KEY ("school_id", "recording_file_id") REFERENCES "file" ("school_id", "id");

ALTER TABLE "lesson_participant" ADD FOREIGN KEY ("school_id", "lesson_id") REFERENCES "lesson" ("school_id", "id");

ALTER TABLE "lesson_participant" ADD FOREIGN KEY ("school_id", "student_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "lesson_material" ADD FOREIGN KEY ("school_id", "lesson_id") REFERENCES "lesson" ("school_id", "id");

ALTER TABLE "lesson_material" ADD FOREIGN KEY ("school_id", "file_id") REFERENCES "file" ("school_id", "id");

ALTER TABLE "pulse_event" ADD FOREIGN KEY ("school_id", "lesson_id") REFERENCES "lesson" ("school_id", "id");

ALTER TABLE "pulse_event" ADD FOREIGN KEY ("school_id", "student_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "doubt_round" ADD FOREIGN KEY ("school_id", "lesson_id") REFERENCES "lesson" ("school_id", "id");

ALTER TABLE "doubt_group" ADD FOREIGN KEY ("school_id", "round_id") REFERENCES "doubt_round" ("school_id", "id");

ALTER TABLE "doubt" ADD FOREIGN KEY ("school_id", "round_id") REFERENCES "doubt_round" ("school_id", "id");

ALTER TABLE "doubt" ADD FOREIGN KEY ("school_id", "student_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "doubt" ADD FOREIGN KEY ("school_id", "group_id") REFERENCES "doubt_group" ("school_id", "id");

ALTER TABLE "floor_round" ADD FOREIGN KEY ("school_id", "lesson_id") REFERENCES "lesson" ("school_id", "id");

ALTER TABLE "floor_turn" ADD FOREIGN KEY ("school_id", "round_id") REFERENCES "floor_round" ("school_id", "id");

ALTER TABLE "floor_turn" ADD FOREIGN KEY ("school_id", "student_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "quiz" ADD FOREIGN KEY ("school_id", "lesson_id") REFERENCES "lesson" ("school_id", "id");

ALTER TABLE "quiz" ADD FOREIGN KEY ("school_id", "created_by_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "quiz" ADD FOREIGN KEY ("school_id", "material_id") REFERENCES "lesson_material" ("school_id", "id");

ALTER TABLE "quiz_answer" ADD FOREIGN KEY ("school_id", "quiz_id") REFERENCES "quiz" ("school_id", "id");

ALTER TABLE "quiz_answer" ADD FOREIGN KEY ("school_id", "student_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "attendance_round" ADD FOREIGN KEY ("school_id", "lesson_id") REFERENCES "lesson" ("school_id", "id");

ALTER TABLE "attendance_record" ADD FOREIGN KEY ("school_id", "round_id") REFERENCES "attendance_round" ("school_id", "id");

ALTER TABLE "attendance_record" ADD FOREIGN KEY ("school_id", "student_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "attendance_record" ADD FOREIGN KEY ("school_id", "reviewed_by_id") REFERENCES "app_user" ("school_id", "id");

ALTER TABLE "student_note" ADD FOREIGN KEY ("school_id", "course_id") REFERENCES "course" ("school_id", "id");

ALTER TABLE "student_note" ADD FOREIGN KEY ("school_id", "student_id") REFERENCES "app_user" ("school_id", "id");
