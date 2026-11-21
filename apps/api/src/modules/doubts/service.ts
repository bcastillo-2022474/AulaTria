import type { ServiceImpl } from "@connectrpc/connect";
import type { doubts } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { openDoubtRound } from "./commands/openDoubtRound.js";
import { extendDoubtRound } from "./commands/extendDoubtRound.js";
import { closeDoubtRound } from "./commands/closeDoubtRound.js";
import { submitDoubt } from "./commands/submitDoubt.js";
import { getDoubtRound } from "./queries/getDoubtRound.js";
import { listDoubts } from "./queries/listDoubts.js";

/** The role table of the module. Writes -> commands/ (entities + repositories), reads -> queries/ (hardcoded SQL). */
export const doubtsService: Partial<ServiceImpl<typeof doubts.DoubtsService>> = {
  openDoubtRound: (req, ctx) => openDoubtRound(actor(ctx, "teacher"), req),
  extendDoubtRound: (req, ctx) => extendDoubtRound(actor(ctx, "teacher"), req),
  closeDoubtRound: (req, ctx) => closeDoubtRound(actor(ctx, "teacher"), req),
  submitDoubt: (req, ctx) => submitDoubt(actor(ctx, "student"), req),
  getDoubtRound: (req, ctx) => getDoubtRound(actor(ctx, "teacher", "student"), req),
  listDoubts: (req, ctx) => listDoubts(actor(ctx, "teacher"), req),
};
