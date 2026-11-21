import { useParams } from "react-router";
import { AppBar, Content, Heading, Screen, Text } from "../../design-system";

/** PLACEHOLDER. TODO(contributor): build Docente-C-Live (docs/design). Skill: new-web-screen. */
export function LivePage() {
  const { lessonId = "" } = useParams();
  return (
    <Screen>
      <AppBar left="AULATRIA" />
      <Content>
        <Heading size={48}>LivePage</Heading>
        <Text as="p" size={16}>Pendiente. Lección {lessonId}</Text>
      </Content>
    </Screen>
  );
}
