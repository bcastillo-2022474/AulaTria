import type { AnchorHTMLAttributes, ButtonHTMLAttributes, ReactNode } from "react";
import { AppLink } from "./AppLink";
import { cx } from "./cx";

export type ButtonVariant = "accent" | "ghost" | "primary" | "ok" | "danger" | "link";
/** Min-height in px, as it appears in the design. 56 is the dominant one. */
/** "lg" is a legacy alias of 60 kept for features/auth/LoginPage. */
export type ButtonSize = "lg" | "48" | "52" | "54" | "56" | "60" | "64" | "68";

// accent/ghost come straight from the design. primary/ok/danger are derived from the student palette (not in any teacher board).
const variantClass: Record<ButtonVariant, string> = {
  accent: "bg-accent text-ink-deep border-b-[5px] border-accent-edge",
  ghost: "bg-stage-ghost text-on-stage",
  primary: "bg-primary text-on-stage border-b-[5px] border-primary-edge",
  ok: "bg-ok text-ok-ink border-b-[5px] border-ok-deep",
  danger: "bg-danger text-on-stage border-b-[5px] border-edge-shade",
  link: "bg-transparent text-on-stage underline underline-offset-4 !min-h-11 !px-0 !text-body-15 !rounded-none",
};
const sizeClass: Record<ButtonSize, string> = {
  "48": "min-h-12 px-5 text-body-16",
  "52": "min-h-[52px] px-7 text-body-16",
  "54": "min-h-[54px] px-7 text-body-16",
  "56": "min-h-14 px-7 text-body-17",
  lg: "min-h-[60px] px-7 text-body-17",
  "60": "min-h-[60px] px-7 text-body-17",
  "64": "min-h-16 px-7 text-body-19",
  "68": "min-h-[68px] px-7 text-body-20",
};

interface Common {
  variant?: ButtonVariant;
  size?: ButtonSize;
  block?: boolean;
  loading?: boolean;
  children: ReactNode;
  className?: string;
}
export type ButtonProps = Common & Omit<ButtonHTMLAttributes<HTMLButtonElement>, keyof Common>;
export type LinkButtonProps = Common & Omit<AnchorHTMLAttributes<HTMLAnchorElement>, keyof Common> & { href: string };

/**
 * Pressable button: Nunito 800, radius 18, thick darker bottom edge (5px) that collapses on press.
 * Renders <a> when `href` is given (the design uses anchors for navigation).
 */
export function Button(props: ButtonProps | LinkButtonProps) {
  const { variant = "accent", size = "56", block, loading, className, children, ...rest } = props;
  const cls = cx(
    "box-border inline-flex items-center justify-center gap-2 rounded-control border-0 font-display font-extrabold no-underline transition-transform",
    sizeClass[size], variantClass[variant], block && "w-full",
    variant !== "ghost" && variant !== "link" && "active:translate-y-[3px] active:border-b-2",
    "aria-disabled:opacity-50 disabled:opacity-50 disabled:active:translate-y-0 disabled:active:border-b-[5px]",
    "cursor-pointer disabled:cursor-not-allowed", className,
  );
  const label = loading ? "…" : children;
  if ("href" in rest && rest.href !== undefined) {
    return <AppLink {...(rest as AnchorHTMLAttributes<HTMLAnchorElement> & { href: string })} className={cls}>{label}</AppLink>;
  }
  const b = rest as ButtonHTMLAttributes<HTMLButtonElement>;
  return <button type="button" {...b} disabled={b.disabled || loading} className={cls}>{label}</button>;
}
