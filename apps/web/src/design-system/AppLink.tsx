import type { AnchorHTMLAttributes } from "react";
import { Link } from "react-router";

/**
 * Every link of the app. Internal paths ("/courses/123", with or without a query string: "/courses?tab=past") navigate
 * client-side through the router (no page reload, state kept); anything else is a normal anchor. A legacy "#/path"
 * is treated as "/path", so older code keeps working.
 */
export function AppLink({ href, ...rest }: AnchorHTMLAttributes<HTMLAnchorElement> & { href: string }) {
  const to = href.startsWith("#/") ? href.slice(1) : href;
  if (to.startsWith("/") && !to.startsWith("//")) return <Link to={to} {...rest} />;
  return <a href={href} {...rest} />;
}
