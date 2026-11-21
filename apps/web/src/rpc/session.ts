import { useSyncExternalStore } from "react";
import { identity } from "@aula/rpc";

/**
 * The signed-in user: tokens + tenant, persisted in localStorage and observable from React.
 * The transport reads it to attach Authorization and X-School-Id; screens read it with `useSession()`.
 */
export interface StoredSession {
  accessToken: string;
  refreshToken: string;
  schoolId: string;
  schoolName: string;
  userId: string;
  name: string;
  role: "admin" | "teacher" | "student";
}

const KEY = "aula.session";
const roleName = (r: identity.Role): StoredSession["role"] =>
  r === identity.Role.ADMIN ? "admin" : r === identity.Role.TEACHER ? "teacher" : "student";

function read(): StoredSession | null {
  try { return JSON.parse(localStorage.getItem(KEY) ?? "null"); } catch { return null; }
}

let current: StoredSession | null = read();
const listeners = new Set<() => void>();
const emit = () => listeners.forEach((l) => l());

export const session = {
  get: () => current,
  /** After Login / RefreshSession / RegisterStudent / AcceptInvitation. */
  set(s: identity.Session) {
    const u = s.user!;
    current = { accessToken: s.accessToken, refreshToken: s.refreshToken, schoolId: u.school!.id, schoolName: u.school!.name, userId: u.id, name: u.name, role: roleName(u.role) };
    localStorage.setItem(KEY, JSON.stringify(current));
    emit();
  },
  clear() { current = null; localStorage.removeItem(KEY); emit(); },
  subscribe(l: () => void) { listeners.add(l); return () => listeners.delete(l); },
};

/** Re-renders when the user signs in or out. `null` = signed out. */
export const useSession = () => useSyncExternalStore(session.subscribe, session.get);
