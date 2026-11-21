import type { ServiceImpl } from "@connectrpc/connect";
import type { followup } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { getStudentParticipation } from "./queries/getStudentParticipation.js";
import { saveStudentNote } from "./commands/saveStudentNote.js";

/** The role table of the module. Writes -> commands/ (entities + repositories), reads -> queries/ (hardcoded SQL). */
export const followupService: Partial<ServiceImpl<typeof followup.FollowupService>> = {
  getStudentParticipation: (req, ctx) => getStudentParticipation(actor(ctx, "teacher"), req),
  saveStudentNote: (req, ctx) => saveStudentNote(actor(ctx, "teacher"), req),
};
