import { createServer } from "node:http";
import { cors } from "@connectrpc/connect";
import { connectNodeAdapter } from "@connectrpc/connect-node";
import { createValidateInterceptor } from "@connectrpc/validate";
import { attendance, courses as coursesRpc, doubts, floor, followup, identity, lessons as lessonsRpc, quizzes } from "@aula/rpc";
import { adminService } from "./modules/identity/adminService.js";
import { attendanceService } from "./modules/attendance/service.js";
import { doubtsService } from "./modules/doubts/service.js";
import { floorService } from "./modules/floor/service.js";
import { followupService } from "./modules/followup/service.js";
import { quizzesService } from "./modules/quizzes/service.js";
import { attachLessonSocket } from "./realtime/websocket.js";
import { authInterceptor, errorInterceptor } from "./shared/context.js";
import { ERROR_CODE_HEADER } from "./shared/errors.js";
import { coursesService } from "./modules/courses/service.js";
import { identityService } from "./modules/identity/service.js";
import { lessonsService } from "./modules/lessons/service.js";

// The router IS the router: one line per service. Every RPC of the contract is wired; the ones nobody has
// written yet answer `unimplemented` from their stub (see modules/*/commands|queries).
const handler = connectNodeAdapter({
  routes(router) {
    router.service(identity.IdentityService, identityService);
    router.service(coursesRpc.CoursesService, coursesService);
    router.service(lessonsRpc.LessonsService, lessonsService);
    router.service(identity.AdminService, adminService);
    router.service(doubts.DoubtsService, doubtsService);
    router.service(quizzes.QuizService, quizzesService);
    router.service(floor.FloorService, floorService);
    router.service(attendance.AttendanceService, attendanceService);
    router.service(followup.FollowupService, followupService);
  },
  // Order matters: errors outermost (logs the raw error, maps it once), then validation, then auth.
  interceptors: [errorInterceptor, createValidateInterceptor(), authInterceptor],
});

const port = Number(process.env.PORT ?? 3000);
const server = createServer((req, res) => {
  // CORS only matters for BROWSER clients (the teacher web app, the Compose browser build): native Android/iOS apps
  // ignore it, which is why Chassis (mobile only) rarely noticed it. Same approach as Chassis' withCORS: the official
  // Connect lists, plus what is ours (Authorization, the tenant header, the error-code header).
  res.setHeader("Access-Control-Allow-Origin", "*"); // dev. In production set the web app's origin.
  res.setHeader("Access-Control-Allow-Methods", cors.allowedMethods.join(", "));
  res.setHeader("Access-Control-Allow-Headers", [...cors.allowedHeaders, "Authorization", "X-School-Id"].join(", "));
  res.setHeader("Access-Control-Expose-Headers", [...cors.exposedHeaders, ERROR_CODE_HEADER].join(", "));
  res.setHeader("Access-Control-Max-Age", "600");
  if (req.method === "OPTIONS") { res.statusCode = 204; return res.end(); }
  handler(req, res);
});
attachLessonSocket(server); // WebSocket realtime (stub for now; WatchLesson is the working stream)
server.listen(port, () => console.log(`api on :${port}`));
