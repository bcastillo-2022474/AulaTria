import { identity } from "@aula/rpc";
import { db } from "../../../db/index.js";
import type { Actor } from "../../../shared/context.js";
import { DeviceRepository } from "../repositories.js";

export async function registerDevice(a: Actor, req: identity.RegisterDeviceRequest) {
  const platform = req.platform === identity.DevicePlatform.IOS ? "ios" : "android";
  await new DeviceRepository(db).upsert(a.schoolId, a.userId, platform, req.pushToken);
  return {};
}
