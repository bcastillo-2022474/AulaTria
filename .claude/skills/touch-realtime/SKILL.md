---
name: touch-realtime
description: Change what the live class pushes to connected clients in AulaTria (WatchLesson stream, LessonSnapshot, hub). Use when a new round/state must show up live on the teacher or student screens.
---

# Realtime

Design in one paragraph: **one server-streaming RPC per lesson, `WatchLesson`** (Connect envelope framing over plain HTTP). The first message is a full `LessonSnapshot`; after every change the server rebuilds the snapshot (debounced 120 ms, once for all viewers) and pushes it, personalised per student (`my_pulse`, `my_submitted`). Clients replace their `GetLessonSnapshot` cache. The stream being open **is** presence (`connected_count`). If the stream is unavailable (UNVERIFIED on iOS and wasm), clients poll `GetLessonSnapshot` (`refetch_seconds`) and nothing else changes. Files: `apps/api/src/modules/lessons/{hub,snapshot,service}.ts`, `apps/web/src/rpc/hooks.ts` (`useLessonStream`), `apps/app/shared/.../realtime/LessonRealtime.kt`.

## To make a new thing appear live
1. **Is it part of "the class right now"?** If yes it belongs in `LessonSnapshot` (a small `XxxRef` message: id, deadline, a counter, maybe a `my_*` flag). If it is a bigger read (a queue, results, groups) put only a **ref** (id) in the snapshot and let the client fetch the details with its own query, which polls via `refetch_seconds` and is invalidated by commands.
2. Add the field to `proto/aula/lessons/v1/lessons.proto` (`LessonSnapshot`); `mage proto:gen`.
3. Fill it in `buildSnapshot` (`snapshot.ts`): one cheap query, **scoped by `school_id`**. Anything per-student goes in `Built` and `forUser()`; teacher-only data must be absent for students.
4. Every command that changes it calls `touch(a.schoolId, lessonId)` after the write. Nothing else is needed: the hub does the rest.
5. Clients: nothing to wire. The snapshot arrives through the existing hook/loop; the screen reads the new field from the query state.
6. The finer-grained events in `LessonEvent` (pulse, round_*) exist for clients that want to patch instead of replace. The server does not emit them yet; do not start without a measured need (it would be a contract-compatible optimisation).

## Rules
- Never push per-student secrets in a shared message; personalise in `forUser`.
- Never block the hub on slow work: `buildSnapshot` must stay a handful of indexed queries. If it grows, count in SQL, do not load rows.
- Never rely on the stream for correctness: every screen must also be right with polling alone. Test by killing the stream (stop reading) mentally: does the next poll fix it?
- Scale limit (accepted): in-process hub, one API instance. LISTEN/NOTIFY is the upgrade path and only `hub.ts` changes.
