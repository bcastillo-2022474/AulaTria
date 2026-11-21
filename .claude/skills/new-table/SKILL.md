---
name: new-table
description: Add or change a database table in AulaTria. DBML first, then the migration, generated Kysely types and seed data. Use whenever persistence changes (new table, new column, new index, new enum value).
---

# New table / schema change

The DBML is the design; Postgres follows it. Order is fixed: **DBML → migration → types → seed**. Reference: `docs/db_schema/db_schema.dbml` (read its header note first).

## 1. Decide before writing
- **Derived values are never stored** (counts, percentages, participation, pulse bars). If you can compute it in a read query, do. 
- **JSON vs table** (Chassis rule): a table when different commands add/edit/delete individual items (each row has its own PK); JSON (`jsonb`) only for a fixed-shape value written whole by one command and never patched (`quiz.options`).
- **Soft delete only where a lifecycle exists** (`course.tp_status`). Rounds/lessons have a status enum, not a deleted flag.
- Append-only for events (`pulse_event`); upsert for "one per student per X" (unique index + `ON CONFLICT`).

## 2. Edit `docs/db_schema/db_schema.dbml`
Mandatory for EVERY table (no exceptions):
1. `school_id uuid [not null, ref: > school.id]` as a column (the tenant, denormalized on every table).
2. Add `(school_id, id) [unique]` to the table's `indexes` if any other table will reference it.
3. For every reference to another table use the **composite** form at the bottom of the file, never an inline `ref`:
   `Ref: child.(school_id, parent_id) > parent.(school_id, id)`. A row must never be able to point at another school's row.
4. `id uuid [pk, not null]`, `created_at timestamptz [not null]` (and `updated_at` only if rows are edited). UTC `timestamptz` always.
5. Enums: `Enum x_status { ... }` (real Postgres enums). Values are lower_snake. Comment each value with the screen meaning.
6. Comment the table: which screens use it, what is derived from it, which invariants live outside the DBML.
7. Add the table to the right `TableGroup`.
8. Unique/partial constraints DBML cannot express (e.g. "one open round per lesson") go in the migration, with a comment saying so in the DBML.

## 3. Migration
- First migration only: `mage db:sql` regenerates `apps/api/migrations/001_init.sql` from the DBML. **Never regenerate 001 once it has been applied anywhere shared.**
- Every later change: a new hand-written file `apps/api/migrations/00N_<what>.sql` (next number, plain SQL, additive-first: nullable column → backfill → constraint; never rename/drop in one step on shared data). Update the DBML in the same change.
- `mage db:migrate` applies it. `mage db:reset db:migrate db:seed` must work from zero.

## 4. Types and seed
- `mage db:types` regenerates `apps/api/src/db/types.ts` (Kysely). Never edit it.
- Update `apps/api/scripts/seed.ts` so the demo has data for the new thing (the seed must stay idempotent and deterministic, and it must delete the new table in dependency order in its wipe list).

## Never
- A table without `school_id`, an inline `ref:`, a stored count/percentage, a migration without the DBML change, editing `types.ts`, `ON DELETE CASCADE` (deletes are explicit, in dependency order).
