import type { ServiceImpl } from "@connectrpc/connect";
import type { identity } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { listTeachers } from "./queries/listTeachers.js";
import { inviteTeacher } from "./commands/inviteTeacher.js";
import { regenerateTeacherInvite } from "./commands/regenerateTeacherInvite.js";

/** Admin-only operations of the school (teachers and their invitations). */
export const adminService: Partial<ServiceImpl<typeof identity.AdminService>> = {
  listTeachers: (req, ctx) => listTeachers(actor(ctx, "admin"), req),
  inviteTeacher: (req, ctx) => inviteTeacher(actor(ctx, "admin"), req),
  regenerateTeacherInvite: (req, ctx) => regenerateTeacherInvite(actor(ctx, "admin"), req),
};
