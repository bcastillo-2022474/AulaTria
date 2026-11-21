import type { ReactNode } from "react";
import { Card } from "./Card";
import { Heading, Text } from "./Typography";

/**
 * "card": centred 520px card, title 30 + body 16 (Admin-C-Catedraticos).
 * "page": open 560px column, title 44 + body 17 + action (Docente-C-CursosVacio).
 */
export function EmptyState({ title, body, action, variant = "card" }: { title: string; body: string; action?: ReactNode; variant?: "card" | "page" }) {
  if (variant === "page") {
    return (
      <div className="flex w-[560px] flex-col items-center gap-[18px] text-center">
        <Heading size={44}>{title}</Heading>
        <Text as="p" size={17}>{body}</Text>
        {action}
      </div>
    );
  }
  return (
    <Card padding={40} className="flex w-[520px] flex-col items-center gap-3 text-center">
      <Heading as="span" size={30}>{title}</Heading>
      <Text as="p" size={16} className="leading-normal">{body}</Text>
    </Card>
  );
}
