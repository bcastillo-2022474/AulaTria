# AulaTria

> **Tu clase tiene más que decir.**

Written from the design export in [`design/`](./design/README.md) (the design is titled "Aula interactiva, Dirección C"; the product is AulaTria). The priorities and the demo are in [MVP.md](./MVP.md).

## What it is

A classroom companion for schools. While the teacher ("catedrático") teaches, students use their phones to give live signal and take part without friction: tap *voy bien* or *me perdí*, write a doubt in 30 seconds, check in to attendance, raise a hand, answer a quiz. The teacher sees the state of the class in one glance, and gets a review afterwards.

The pitch in one line, from the MVP: the teacher **sees understanding in real time**, not at the exam; students take part with one tap; and the app **turns confusion into action**: grouped doubts, a quiz drafted from the slides, a review of where the class got lost.

## Who

- **School admin:** sets up the school's account and invites teachers.
- **Teacher:** creates courses, opens a room, runs the interactions, reviews the pulse and each student's participation.
- **Student:** joins a course with a code (which also tells the app their school), and participates from the phone.

## Core features

1. **Onboarding.** The admin invites teachers by email. Students join with a course code that also identifies their school.
2. **Courses and rooms.** A teacher creates a course (join code and QR), opens a *sala* (the room, where enrolled students see it is live), then starts the class.
3. **Pulse.** Students signal *voy bien* or *me perdí*. The teacher sees only the aggregate, per minute.
4. **Doubt round (30 s).** Students write a doubt. When the round closes, similar doubts are grouped into the top topics and linked to the slides.
5. **Attendance.** A rotating QR and a short typed code, plus a location check. Clear cases are automatic. Borderline cases (too far, no location) go to the teacher for review.
6. **Floor.** The teacher poses a question. The first student to raise a hand answers. The teacher marks it correct or incorrect, and the next student in the queue goes.
7. **Quiz.** A question drafted from the lesson material, launched live, with counts per option and a result.
8. **Review.** A pulse timeline with quiz and doubt markers, and a participation history per student with a private teacher note.

## Principles visible in the design

- **Students see no one else's data.** The teacher sees aggregates, except where one individual's action is the point (the floor queue, the attendance review).
- **Participation is not comprehension.** The profile screen says so ("Tu criterio cuenta"). The product informs the teacher's judgement; it does not grade.
- **The server decides.** Quiz correctness, attendance status and turn order are computed on the server, never on the device.
- **Big, glanceable, one-hand targets for students; dense but calm for the teacher.**

## Scope

- **In the MVP:** the live loop, the review of a past class, doubt rounds with grouping, the live quiz, the floor, attendance with a typed code, and onboarding. Priorities and the cut line are in [MVP.md](./MVP.md).
- **Not in the MVP:** class recording, activities and extra points, offline use and sync, push notifications (later), per-school branding.
- **Online only.** No offline mode.
- **Open product questions** (recording, roster, material upload, minors' data and others) are in [CONTRACT.md section 8](./CONTRACT.md#8-open-questions).
