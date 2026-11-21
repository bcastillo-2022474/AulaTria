import type { ServiceImpl } from "@connectrpc/connect";
import type { lessons } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { closeRoom } from "./commands/closeRoom.js";
import { endLesson } from "./commands/endLesson.js";
import { joinRoom } from "./commands/joinRoom.js";
import { openRoom } from "./commands/openRoom.js";
import { setPulse } from "./commands/setPulse.js";
import { startLesson } from "./commands/startLesson.js";
import { getLessonSnapshot } from "./queries/getLessonSnapshot.js";
import { watchLesson } from "./queries/watchLesson.js";
import { getLessonReview } from "./queries/getLessonReview.js";
import { uploadMaterial } from "./commands/uploadMaterial.js";
import { listMaterials } from "./queries/listMaterials.js";

/** The role table of the module. Writes -> commands/ (entities + repositories), reads -> queries/ (hardcoded SQL). */
export const lessonsService: Partial<ServiceImpl<typeof lessons.LessonsService>> = {
  // teacher
  openRoom: (req, ctx) => openRoom(actor(ctx, "teacher"), req),
  closeRoom: (req, ctx) => closeRoom(actor(ctx, "teacher"), req),
  startLesson: (req, ctx) => startLesson(actor(ctx, "teacher"), req),
  endLesson: (req, ctx) => endLesson(actor(ctx, "teacher"), req),
  // student
  joinRoom: (req, ctx) => joinRoom(actor(ctx, "student"), req),
  setPulse: (req, ctx) => setPulse(actor(ctx, "student"), req),
  // both
  getLessonSnapshot: (req, ctx) => getLessonSnapshot(actor(ctx, "teacher", "student"), req),
  watchLesson: (req, ctx) => watchLesson(actor(ctx, "teacher", "student"), req, ctx.signal),
  getLessonReview: (req, ctx) => getLessonReview(actor(ctx, "teacher"), req),
  uploadMaterial: (req, ctx) => uploadMaterial(actor(ctx, "teacher"), req),
  listMaterials: (req, ctx) => listMaterials(actor(ctx, "teacher"), req),
};
