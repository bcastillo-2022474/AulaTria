import type { ServiceImpl } from "@connectrpc/connect";
import type { courses } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { createCourse } from "./commands/createCourse.js";
import { joinCourse } from "./commands/joinCourse.js";
import { getCourse } from "./queries/getCourse.js";
import { listCourseStudents } from "./queries/listCourseStudents.js";
import { listCourses } from "./queries/listCourses.js";

/** The role table of the module. Writes -> commands/, reads -> queries/. */
export const coursesService: Partial<ServiceImpl<typeof courses.CoursesService>> = {
  listCourses: (_req, ctx) => listCourses(actor(ctx, "teacher", "student")),
  getCourse: (req, ctx) => getCourse(actor(ctx, "teacher", "student"), req),
  listCourseStudents: (req, ctx) => listCourseStudents(actor(ctx, "teacher"), req),
  createCourse: (req, ctx) => createCourse(actor(ctx, "teacher"), req),
  joinCourse: (req, ctx) => joinCourse(actor(ctx, "student"), req),
};
