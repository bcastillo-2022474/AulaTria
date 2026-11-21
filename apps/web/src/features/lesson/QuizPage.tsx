import { useParams } from "react-router";
import { AppBar, Content, Heading, Screen, Text } from "../../design-system";

/** PLACEHOLDER. TODO(contributor): build Docente-C-QuizArmar / QuizVivo / QuizResultados (docs/design). Skill: new-web-screen. */
export function QuizPage() {
  const { lessonId = "" } = useParams();
  return (
    <Screen>
      <AppBar left="AULATRIA" />
      <Content>
        <Heading size={48}>QuizPage</Heading>
        <Text as="p" size={16}>Pendiente. Lección {lessonId}</Text>
      </Content>
    </Screen>
  );
}
