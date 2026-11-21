import { doubts } from "@aula/rpc";
import type { MessageInitShape } from "@bufbuild/protobuf";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { ts, tsOrUndef } from "../../../shared/ids.js";
import { visibleDoubtRound } from "./visibility.js";

const count = async (q: { executeTakeFirstOrThrow(): Promise<{ n: unknown }> }) => Number((await q.executeTakeFirstOrThrow()).n);

/**
 * READ: the round as this actor sees it. One message for both roles; role projection:
 *  - teacher: the aggregate (counts and, once the summary is READY, the groups). Never who wrote what.
 *  - student: their own doubt and their own group. Never the groups list, other students' doubts or the counts per group.
 * A round past its deadline is CLOSED here even if the expiry has not been persisted yet (the server's clock decides).
 */
export async function getDoubtRound(a: Actor, req: doubts.GetDoubtRoundRequest): Promise<{ round: MessageInitShape<typeof doubts.DoubtRoundSchema> }> {
  const round = await visibleDoubtRound(a, req.roundId);
  const now = new Date();
  const open = round.status === "open" && round.closes_at.getTime() > now.getTime();

  const submitted = await count(db.selectFrom("doubt").select((e) => e.fn.countAll().as("n")).where("school_id", "=", a.schoolId).where("round_id", "=", round.id));
  const enrolled = await count(db.selectFrom("enrollment").select((e) => e.fn.countAll().as("n")).where("school_id", "=", a.schoolId).where("course_id", "=", round.course_id));

  const message: MessageInitShape<typeof doubts.DoubtRoundSchema> = {
    id: round.id,
    lessonId: round.lesson_id,
    status: open ? doubts.DoubtRoundStatus.OPEN : doubts.DoubtRoundStatus.CLOSED,
    openedAt: ts(round.opened_at),
    closesAt: ts(round.closes_at),
    submittedCount: submitted,
    enrolledCount: enrolled,
    summaryStatus: summaryStatusToProto(round.summary_status),
  };

  // Groups exist only once the grouping finished. student_count is derived: the doubts pointing at the group.
  const groups = round.summary_status === "ready"
    ? await db.selectFrom("doubt_group")
      .leftJoin("doubt", (j) => j.onRef("doubt.group_id", "=", "doubt_group.id").onRef("doubt.school_id", "=", "doubt_group.school_id"))
      .select(["doubt_group.id", "doubt_group.rank", "doubt_group.title", "doubt_group.slide_number", "doubt_group.sample_quote", (e) => e.fn.count("doubt.id").as("n")])
      .where("doubt_group.school_id", "=", a.schoolId).where("doubt_group.round_id", "=", round.id)
      .groupBy("doubt_group.id").orderBy("doubt_group.rank").execute()
    : [];
  const toGroup = (g: (typeof groups)[number]) => ({ id: g.id, rank: g.rank, title: g.title, slideNumber: g.slide_number ?? 0, studentCount: Number(g.n), sampleQuote: g.sample_quote ?? "" });

  if (a.role === "teacher") {
    message.groups = groups.map(toGroup);
  } else if (a.role === "student") {
    const mine = await db.selectFrom("doubt").select(["text", "group_id"])
      .where("school_id", "=", a.schoolId).where("round_id", "=", round.id).where("student_id", "=", a.userId).executeTakeFirst();
    if (mine) {
      // Editable until the deadline, or until the teacher closed it if that came first.
      const until = round.closed_at && round.closed_at < round.closes_at ? round.closed_at : round.closes_at;
      message.myDoubt = { text: mine.text, editableUntil: tsOrUndef(until) };
      const group = mine.group_id ? groups.find((g) => g.id === mine.group_id) : undefined;
      if (group) message.myGroup = toGroup(group);
    }
  }
  return { round: message };
}

/** DB state -> proto enum. Explicit on purpose: a new DB state must fail loudly here, not map to 0. */
function summaryStatusToProto(status: string): doubts.SummaryStatus {
  switch (status) {
    case "pending": return doubts.SummaryStatus.PENDING;
    case "ready": return doubts.SummaryStatus.READY;
    case "failed": return doubts.SummaryStatus.FAILED;
    default: throw new Error(`unknown doubt summary status ${status}`);
  }
}
