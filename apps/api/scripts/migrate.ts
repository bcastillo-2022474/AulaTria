// Applies apps/api/migrations/*.sql in name order, once each. No framework: the DBML is the design,
// 001 is generated from it (`mage db:sql`), later ones are written by hand and the DBML is updated
// in the same change.
import { readdirSync, readFileSync } from "node:fs";
import pg from "pg";

const dir = new URL("../migrations/", import.meta.url);
const client = new pg.Client({ connectionString: process.env.DATABASE_URL });
await client.connect();
await client.query("create table if not exists _migrations (name text primary key, applied_at timestamptz not null default now())");
const done = new Set((await client.query("select name from _migrations")).rows.map((r) => r.name));
for (const file of readdirSync(dir).filter((f) => f.endsWith(".sql")).sort()) {
  if (done.has(file)) continue;
  console.log("applying", file);
  await client.query("begin");
  try {
    await client.query(readFileSync(new URL(file, dir), "utf8"));
    await client.query("insert into _migrations (name) values ($1)", [file]);
    await client.query("commit");
  } catch (e) {
    await client.query("rollback");
    throw e;
  }
}
await client.end();
console.log("migrations up to date");
