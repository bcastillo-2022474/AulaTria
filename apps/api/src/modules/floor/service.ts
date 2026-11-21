import type { ServiceImpl } from "@connectrpc/connect";
import type { floor } from "@aula/rpc";
import { actor } from "../../shared/context.js";
import { openFloorRound } from "./commands/openFloorRound.js";
import { raiseHand } from "./commands/raiseHand.js";
import { lowerHand } from "./commands/lowerHand.js";
import { finishMyTurn } from "./commands/finishMyTurn.js";
import { resolveTurn } from "./commands/resolveTurn.js";
import { openFloorToAll } from "./commands/openFloorToAll.js";
import { closeFloorRound } from "./commands/closeFloorRound.js";
import { getFloorRound } from "./queries/getFloorRound.js";

/** The role table of the module. Writes -> commands/ (entities + repositories), reads -> queries/ (hardcoded SQL). */
export const floorService: Partial<ServiceImpl<typeof floor.FloorService>> = {
  openFloorRound: (req, ctx) => openFloorRound(actor(ctx, "teacher"), req),
  raiseHand: (req, ctx) => raiseHand(actor(ctx, "student"), req),
  lowerHand: (req, ctx) => lowerHand(actor(ctx, "student"), req),
  finishMyTurn: (req, ctx) => finishMyTurn(actor(ctx, "student"), req),
  resolveTurn: (req, ctx) => resolveTurn(actor(ctx, "teacher"), req),
  openFloorToAll: (req, ctx) => openFloorToAll(actor(ctx, "teacher"), req),
  closeFloorRound: (req, ctx) => closeFloorRound(actor(ctx, "teacher"), req),
  getFloorRound: (req, ctx) => getFloorRound(actor(ctx, "teacher", "student"), req),
};
