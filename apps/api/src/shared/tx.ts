import type { Kysely, Transaction } from "kysely";
import { db } from "../db/index.js";
import type { DB } from "../db/types.js";

/** What a repository is built with: the pool, or the transaction of the command that uses it. */
export type Executor = Kysely<DB> | Transaction<DB>;

/**
 * One transaction per command. Repositories are constructed INSIDE it (`new LessonRepository(trx)`), which is the
 * whole "unit of work": no framework, no generic, nothing that outlives the callback.
 */
export const inTx = <T>(fn: (trx: Transaction<DB>) => Promise<T>): Promise<T> => db.transaction().execute(fn);
