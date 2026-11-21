// Sets the password of EVERY account to $SEED_PASSWORD, in place (no data is touched). Ops tool for the demo deployment:
// start.sh runs it when RESET_PASSWORDS=1 (set the variable, deploy, remove the variable).
import bcrypt from "bcryptjs";
import { db } from "../src/db/index.js";

const password = process.env.SEED_PASSWORD;
if (!password) throw new Error("SEED_PASSWORD is required");
const hash = await bcrypt.hash(password, 10);
const res = await db.updateTable("app_user").set({ password_hash: hash }).where("password_hash", "is not", null).executeTakeFirst();
console.log(`passwords reset for ${res.numUpdatedRows} accounts`);
await db.destroy();
