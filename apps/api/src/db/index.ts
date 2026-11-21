import { Kysely, PostgresDialect } from "kysely";
import pg from "pg";
import type { DB } from "./types.js";

pg.types.setTypeParser(20, (v) => Number(v)); // bigint counts -> number

export const db = new Kysely<DB>({
  dialect: new PostgresDialect({ pool: new pg.Pool({ connectionString: process.env.DATABASE_URL, max: 10 }) }),
});

/** Postgres unique-violation, to turn a racing insert into a domain error. */
export const isUniqueViolation = (e: unknown) => (e as { code?: string })?.code === "23505";
