import { Card } from "./Card";
import { Heading } from "./Typography";

/** Label + value card ("Tiempo por pregunta  45 s"). Display only in the design: no stepper exists there. */
export function SettingRow({ label, value }: { label: string; value: string }) {
  return (
    <Card padding={24} className="flex items-center justify-between">
      <span className="text-body-15">{label}</span>
      <Heading as="span" size={24}>{value}</Heading>
    </Card>
  );
}
