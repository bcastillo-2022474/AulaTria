import { useState } from "react";
import {
  ActionTile, AppBar, Badge, Banner, Button, Card, CodeBox, ColumnChart, Content, CountPill, Countdown, CourseTile,
  EmptyState, Eyebrow, Field, Heading, LetterBadge, ListGroup, ListRow, LogoSlot, Meter, OPTION_COLORS, OptionRow,
  PlayIcon, Progress, PulseChart, QrFrame, QueueRow, Screen, SettingRow, Stat, StatTile, StatusDot, Text, TextArea, VideoPanel,
} from "./index";
import type { BrandColor, ButtonSize, ButtonVariant } from "./index";

const PULSE = [84,83,83,82,82,82,81,81,81,81,80,80,79,78,78,77,76,75,73,72,71,67,64,61,57,55,51,47,44,41,38,40,41,43,45,47,51,54,58,61,64,64,65,66,68,69,70,71,72,72,73];
const OPTIONS = ["Disminuye: hay más rutas de acceso", "Se mantiene igual", "Aumenta: cada índice también se actualiza", "Solo aumenta con índices hash"];
const VOTES = [3, 6, 14, 6];
const BRAND: readonly BrandColor[] = OPTION_COLORS;

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  return (
    <section className="flex flex-col gap-4">
      <h2 className="type-title m-0 border-b border-stage-divider pb-2 text-title-24">{title}</h2>
      <div className="flex flex-wrap items-start gap-4">{children}</div>
    </section>
  );
}
function Cell({ name, children, wide }: { name: string; children: React.ReactNode; wide?: boolean }) {
  return (
    <div className={`flex flex-col gap-2 ${wide ? "w-full" : ""}`}>
      <span className="text-body-12 text-stage-ink">{name}</span>
      {children}
    </div>
  );
}

function OptionList({ votes, correct, dimmed, height }: { votes?: boolean; correct?: number; dimmed?: boolean; height?: 56 | 60 }) {
  return (
    <div className="flex flex-col gap-2.5">
      {OPTIONS.map((t, i) => (
        <OptionRow key={t} letter={"ABCD"[i]!} color={BRAND[i]!} text={t} height={height}
          correct={correct === i} dimmed={dimmed && correct !== i}
          count={votes ? VOTES[i] : undefined} share={votes ? VOTES[i]! / 14 : undefined} />
      ))}
    </div>
  );
}

/* ---------- Reproduced screens (composed only from design-system components) ---------- */

function ScreenLive() {
  return (
    <Screen frame>
      <AppBar left="CC3057 · CLASE 12" right={<><StatusDot /> Grabando · 31:20</>} />
      <Content className="!gap-6">
        <div className="flex flex-col gap-1.5">
          <Heading size={44}>Clase en curso</Heading>
          <Text size={15}>36 de 38 conectados</Text>
        </div>
        <Card className="flex flex-col gap-4">
          <div className="flex items-baseline justify-between">
            <Stat value="84%" caption="voy bien" size={64} />
            <Text size={16}>6 marcaron «me perdí»</Text>
          </div>
          <Meter value={84.2} lost={15.8} label="Pulso de la clase" />
        </Card>
        <div className="grid min-h-0 flex-1 grid-cols-4 gap-5">
          <ActionTile href="#/design-system" color="danger" letter="D" title="Preguntar dudas" subtitle="30 segundos" />
          <ActionTile href="#/design-system" color="info" letter="P" title="Dar la palabra" subtitle="El primero en pedirla responde" />
          <ActionTile href="#/design-system" color="sun" letter="Q" title="Lanzar quiz" subtitle="Pregunta lista desde tu material" />
          <ActionTile href="#/design-system" color="ok-deep" letter="A" title="Tomar asistencia" subtitle="QR con ubicación" />
        </div>
        <div className="flex justify-end"><Button href="#/design-system" variant="ghost" size="52">Terminar clase y ver repaso →</Button></div>
      </Content>
    </Screen>
  );
}

function ScreenQuizVivo() {
  return (
    <Screen frame>
      <AppBar left="CC3057 · CLASE 12" right="Quiz en vivo" />
      <Content className="!gap-[22px]">
        <div className="flex items-end justify-between gap-10">
          <div className="flex flex-1 flex-col gap-2.5">
            <Eyebrow>Quiz · en vivo</Eyebrow>
            <Heading as="h2" size={36} className="text-pretty">¿Qué ocurre con el costo de un INSERT cuando una tabla tiene muchos índices?</Heading>
          </div>
          <div className="flex w-[260px] flex-none flex-col gap-2.5">
            <div className="flex items-baseline justify-between">
              <Countdown seconds={27} size={48} />
              <Text size={14}>29 de 38</Text>
            </div>
            <Progress value={60} label="Tiempo restante" />
          </div>
        </div>
        <OptionList votes />
        <div className="mt-auto flex justify-end"><Button href="#/design-system" size="56">Mostrar resultado</Button></div>
      </Content>
    </Screen>
  );
}

function ScreenCursos() {
  return (
    <Screen frame>
      <AppBar left="CC3057 · CLASE 12" right="Mis cursos" />
      <Content>
        <div className="flex items-center justify-between">
          <Heading size={48}>Mis cursos</Heading>
          <Button href="#/design-system" size="56">+ Crear curso</Button>
        </div>
        <div className="grid grid-cols-3 gap-5">
          <CourseTile href="#/design-system" color="info" code="CC3057" name="Bases de Datos 2" meta="38 estudiantes" />
          <CourseTile href="#/design-system" color="ok-deep" code="CC3064" name="Sistemas Operativos" meta="31 estudiantes" />
        </div>
      </Content>
    </Screen>
  );
}

function ScreenAdminLista() {
  return (
    <Screen frame>
      <AppBar left="CC3057 · CLASE 12" right="Administración" />
      <Content>
        <div className="flex items-center justify-between">
          <Heading size={48}>Catedráticos</Heading>
          <Button href="#/design-system" size="56">+ Agregar catedrático</Button>
        </div>
        <Banner>Invitación enviada a luis.perez@sanmarcos.edu.gt</Banner>
        <ListGroup>
          <ListRow density="76" title="Luis Pérez" subtitle="luis.perez@sanmarcos.edu.gt"
            trailing={<><Badge tone="accent">Invitación pendiente</Badge><Button href="#/design-system" variant="link">Copiar enlace</Button></>} />
          <ListRow density="76" title="Ana Morales" subtitle="ana.morales@sanmarcos.edu.gt" trailing={<Badge>Activo</Badge>} />
          <ListRow density="76" title="Carlos Ruiz" subtitle="carlos.ruiz@sanmarcos.edu.gt" trailing={<Badge>Activo</Badge>} />
        </ListGroup>
      </Content>
    </Screen>
  );
}

function ScreenPalabra() {
  return (
    <Screen frame>
      <AppBar left="CC3057 · CLASE 12" right="Toma de la palabra" />
      <Content>
        <div className="flex min-h-0 flex-1 gap-10">
          <div className="flex min-w-0 flex-1 flex-col gap-6">
            <div className="flex flex-col gap-2.5">
              <Eyebrow>Pregunta</Eyebrow>
              <Heading as="h2" size={38} className="text-pretty">¿Qué información devuelve EXPLAIN ANALYZE que EXPLAIN solo no da?</Heading>
            </div>
            <Card className="flex flex-col gap-[18px]">
              <div className="flex items-baseline justify-between"><Eyebrow>Responde ahora</Eyebrow><Countdown seconds={12} size={40} /></div>
              <Heading as="span" size={56}>Marta R.</Heading>
              <Progress value={60} label="Tiempo para responder" />
              <div className="mt-1.5 flex gap-3">
                <Button href="#/design-system" size="56">Acertó</Button>
                <Button href="#/design-system" variant="ghost" size="56">No acertó · siguiente</Button>
              </div>
            </Card>
          </div>
          <div className="flex w-[420px] flex-none flex-col gap-3">
            <Eyebrow>Fila · 4 pidieron la palabra</Eyebrow>
            <ul className="m-0 flex flex-col gap-2 p-0">
              <QueueRow rank={1} name="Marta R." offset="+0,8 s" current />
              <QueueRow rank={2} name="Ana Lucía M." offset="+1,2 s" />
              <QueueRow rank={3} name="Diego A." offset="+1,9 s" />
              <QueueRow rank={4} name="Sofía P." offset="+2,4 s" />
            </ul>
            <div className="mt-auto"><Button href="#/design-system" variant="ghost" size="52" block>Abrir a todos</Button></div>
          </div>
        </div>
      </Content>
    </Screen>
  );
}

/* ---------- Gallery ---------- */

export function Gallery() {
  const [pending, setPending] = useState(false);
  const variants: ButtonVariant[] = ["accent", "ghost", "primary", "ok", "danger", "link"];
  const sizes: ButtonSize[] = ["48", "52", "54", "56", "60", "64", "68"];
  return (
    <Screen>
      <AppBar left="DESIGN SYSTEM · STAGE" brand={<LogoSlot />} right="web (docente y admin)" />
      <div className="mx-auto flex w-full max-w-[1320px] flex-col gap-12 px-10 pb-20">
        <Heading size={48}>Componentes</Heading>

        <Section title="Marca">
          <Cell name="LogoSlot (reemplazar con docs/brand/logo-*.png)"><LogoSlot /></Cell>
          <Cell name="Puntos de marca = colores de tile">
            <div className="flex gap-2">{BRAND.map((c) => <CountPill key={c} color={c}>{c}</CountPill>)}</div>
          </Cell>
        </Section>

        <Section title="Tipografía">
          <Cell name="Heading 58/48/36/28/20 (Nunito 900)" wide>
            <div className="flex flex-col gap-2">{([58, 48, 36, 28, 20] as const).map((s) => <Heading key={s} size={s} as="span">Tu clase tiene más que decir · {s}</Heading>)}</div>
          </Cell>
          <Cell name="Eyebrow"><Eyebrow>CLASES ANTERIORES</Eyebrow></Cell>
          <Cell name="Text 14-18"><div className="flex flex-col gap-1">{([14, 15, 16, 17, 18] as const).map((s) => <Text key={s} size={s}>Texto secundario {s}px</Text>)}</div></Cell>
        </Section>

        <Section title="Button: variantes x estados">
          {variants.map((v) => (
            <Cell key={v} name={v}>
              <div className="flex gap-3">
                <Button variant={v}>Continuar</Button>
                <Button variant={v} disabled>Deshabilitado</Button>
                <Button variant={v} loading>Cargando</Button>
              </div>
            </Cell>
          ))}
          <Cell name="Tamaños (alto mínimo en px)" wide>
            <div className="flex flex-wrap items-center gap-3">{sizes.map((s) => <Button key={s} size={s}>{s}</Button>)}</div>
          </Cell>
          <Cell name="block"><div className="w-[300px]"><Button block variant="ghost" size="52">Repasar este tramo</Button></div></Cell>
          <Cell name="Estado cargando interactivo"><Button onClick={() => { setPending(true); setTimeout(() => setPending(false), 1200); }} loading={pending}>Pulsa</Button></Cell>
        </Section>

        <Section title="Field">
          <div className="w-[420px]"><Cell name="normal (60)"><Field label="Nombre completo" defaultValue="Luis Pérez" /></Cell></div>
          <div className="w-[420px]"><Cell name="placeholder"><Field label="Contraseña nueva" type="password" placeholder="Mínimo 8 caracteres" /></Cell></div>
          <div className="w-[420px]"><Cell name="solo lectura"><Field label="Correo" readOnly defaultValue="admin@sanmarcos.edu.gt" /></Cell></div>
          <div className="w-[420px]"><Cell name="grande (64) + hint"><Field label="Nombre del curso" size="64" defaultValue="Bases de Datos 2" hint="Tus estudiantes verán este nombre al unirse." /></Cell></div>
          <div className="w-[420px]"><Cell name="error (derivado)"><Field label="Contraseña" type="password" error="Mínimo 8 caracteres" /></Cell></div>
          <div className="flex h-[220px] w-[350px] flex-col gap-4"><Cell name="TextArea"><TextArea label="NOTA DEL CATEDRÁTICO" defaultValue="Falta a clases por trabajo." /></Cell></div>
        </Section>

        <Section title="Card, Badge, Banner">
          <Cell name="Card stage padding 28"><Card className="w-[260px]">Contenido</Card></Cell>
          <Cell name="Card paper (paridad estudiante)"><Card tone="paper" className="w-[260px]">Contenido</Card></Cell>
          <Cell name="Badge"><div className="flex gap-2"><Badge>Activo</Badge><Badge tone="accent">Invitación pendiente</Badge><Badge tone="chart">Quiz</Badge></div></Cell>
          <Cell name="CountPill"><div className="flex gap-2"><CountPill color="danger">14 estudiantes</CountPill><CountPill color="info">10 estudiantes</CountPill><CountPill color="sun">7 estudiantes</CountPill></div></Cell>
          <Cell name="Banner ok / warn / danger" wide>
            <div className="flex flex-col gap-2"><Banner>Invitación enviada a luis.perez@sanmarcos.edu.gt</Banner><Banner tone="warn">Sin conexión, reintentando</Banner><Banner tone="danger">No se pudo enviar la invitación</Banner></div>
          </Cell>
        </Section>

        <Section title="Tiles">
          <Cell name="ActionTile x4" wide>
            <div className="grid grid-cols-4 gap-5">
              <ActionTile color="danger" letter="D" title="Preguntar dudas" subtitle="30 segundos" />
              <ActionTile color="info" letter="P" title="Dar la palabra" subtitle="El primero en pedirla responde" />
              <ActionTile color="sun" letter="Q" title="Lanzar quiz" subtitle="Pregunta lista desde tu material" />
              <ActionTile color="ok-deep" letter="A" title="Tomar asistencia" subtitle="QR con ubicación" />
            </div>
          </Cell>
          <Cell name="CourseTile"><div className="w-[380px]"><CourseTile color="info" code="CC3057" name="Bases de Datos 2" meta="38 estudiantes" /></div></Cell>
          <Cell name="StatTile x4">
            <div className="grid w-[460px] grid-cols-2 gap-3.5">
              <StatTile color="danger" value="11/12" caption="clases con asistencia" />
              <StatTile color="info" value="9" caption="dudas enviadas" />
              <StatTile color="sun" value="4" caption="veces tomó la palabra, 3 acertó" />
              <StatTile color="ok-deep" value="8/10" caption="quizzes en los que participó" />
            </div>
          </Cell>
          <Cell name="LetterBadge"><div className="flex gap-3 bg-danger p-3"><LetterBadge letter="D" /><LetterBadge letter="A" shape="circle" /></div></Cell>
        </Section>

        <Section title="Stat, Meter, Progress, Countdown">
          <Cell name="Stat 64"><Stat value="84%" caption="voy bien" /></Cell>
          <Cell name="Stat 110 accent"><Stat value="27" caption="de 38 presentes" size={110} captionSize={24} accent /></Cell>
          <Cell name="Meter 84 / 16"><div className="w-[360px]"><Meter value={84.2} lost={15.8} label="Pulso" /></div></Cell>
          <Cell name="Meter 63 / 8 (sin completar)"><div className="w-[360px]"><Meter value={63.2} lost={7.9} label="Presentes" /></div></Cell>
          <Cell name="Progress 60"><div className="w-[260px]"><Progress value={60} label="Tiempo" /></div></Cell>
          <Cell name="Countdown 48 / 40"><div className="flex gap-6"><Countdown seconds={27} /><Countdown seconds={12} size={40} /></div></Cell>
          <Cell name="Countdown 200"><Countdown seconds={18} size={200} /></Cell>
        </Section>

        <Section title="Quiz: OptionRow">
          <div className="w-[560px]"><Cell name="borrador (con correcta)"><OptionList correct={2} /></Cell></div>
          <div className="w-[560px]"><Cell name="en vivo (barra de votos)"><OptionList votes /></Cell></div>
          <div className="w-[560px]"><Cell name="resultados (56, incorrectas al 50%)"><OptionList votes correct={2} dimmed height={56} /></Cell></div>
        </Section>

        <Section title="Cola, listas, vacío">
          <Cell name="QueueRow"><ul className="m-0 flex w-[420px] flex-col gap-2 p-0"><QueueRow rank={1} name="Marta R." offset="+0,8 s" current /><QueueRow rank={2} name="Ana Lucía M." offset="+1,2 s" /></ul></Cell>
          <Cell name="ListGroup 64 con chevron"><div className="w-[440px]"><ListGroup><ListRow title="Clase 11" subtitle="18 nov · 36 asistentes" chevron href="#/design-system" /><ListRow title="Clase 10" subtitle="14 nov · 35 asistentes" chevron href="#/design-system" /></ListGroup></div></Cell>
          <Cell name="EmptyState card"><EmptyState title="Aún no hay catedráticos" body="Agrega al primero para que pueda crear sus cursos y abrir salas." /></Cell>
          <Cell name="EmptyState page"><EmptyState variant="page" title="Crea tu primer curso" body="Solo necesitas un nombre. Después compartes un código con tus estudiantes." action={<Button size="64" className="min-w-[260px]">Crear curso</Button>} /></Cell>
          <Cell name="SettingRow"><div className="w-[360px]"><SettingRow label="Tiempo por pregunta" value="45 s" /></div></Cell>
        </Section>

        <Section title="QR, código, vídeo, gráficas">
          <Cell name="QrFrame large"><QrFrame large /></Cell>
          <Cell name="QrFrame"><QrFrame /></Cell>
          <Cell name="CodeBox"><CodeBox code="BD2-4K9P" /></Cell>
          <Cell name="VideoPanel"><div className="flex h-[260px] w-[560px]"><VideoPanel caption="Grabación de pantalla y audio del catedrático" timecode="31:20 / 50:00" /></div></Cell>
          <Cell name="PulseChart" wide><div className="flex h-[260px]"><PulseChart values={PULSE} markers={[{ at: 24, label: "Quiz" }, { at: 62, label: "Dudas" }, { at: 88, label: "Dudas" }]} playhead={62.67} /></div></Cell>
          <Cell name="ColumnChart"><Card padding={20} className="w-[560px]"><ColumnChart values={[3,5,4,6,0,7,5,8,6,9,7,8]} labels={["1","2","3","4","5","6","7","8","9","10","11","12"]} /></Card></Cell>
          <Cell name="PlayIcon"><span className="text-accent"><PlayIcon /></span></Cell>
        </Section>

        <Section title="Pantallas completas (1280x800, comparar con docs/design)">
          {[["live", "Docente-C-Live", <ScreenLive key="l" />], ["quizvivo", "Docente-C-QuizVivo", <ScreenQuizVivo key="q" />],
            ["cursos", "Docente-C-Cursos", <ScreenCursos key="c" />], ["adminlista", "Admin-C-Lista", <ScreenAdminLista key="a" />],
            ["palabra", "Docente-C-Palabra", <ScreenPalabra key="p" />]].map(([id, name, el]) => (
            <Cell key={id as string} name={name as string}><div id={`screen-${id as string}`} className="w-[1280px] overflow-hidden">{el}</div></Cell>
          ))}
        </Section>
      </div>
    </Screen>
  );
}
