import { timestampFromDate } from "@bufbuild/protobuf/wkt";
import { uuidv7 } from "uuidv7";

export const newId = () => uuidv7();
export const ts = (d: Date) => timestampFromDate(d);
export const tsOrUndef = (d: Date | null | undefined) => (d ? timestampFromDate(d) : undefined);
