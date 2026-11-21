import { useParams } from "react-router";
import { AppBar, Content, Heading, Screen, Text } from "../../design-system";

/** PLACEHOLDER. TODO(contributor): build Docente-C-SalaAbierta (docs/design). Skill: new-web-screen. */
export function RoomPage() {
  const { lessonId = "" } = useParams();
  return (
    <Screen>
      <AppBar left="AULATRIA" />
      <Content>
        <Heading size={48}>RoomPage</Heading>
        <Text as="p" size={16}>Pendiente. Lección {lessonId}</Text>
      </Content>
    </Screen>
  );
}
