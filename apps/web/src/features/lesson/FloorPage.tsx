import { useParams } from "react-router";
import { AppBar, Content, Heading, Screen, Text } from "../../design-system";

/** PLACEHOLDER. TODO(contributor): build Docente-C-Palabra (docs/design). Skill: new-web-screen. */
export function FloorPage() {
  const { lessonId = "" } = useParams();
  return (
    <Screen>
      <AppBar left="AULATRIA" />
      <Content>
        <Heading size={48}>FloorPage</Heading>
        <Text as="p" size={16}>Pendiente. Lección {lessonId}</Text>
      </Content>
    </Screen>
  );
}
