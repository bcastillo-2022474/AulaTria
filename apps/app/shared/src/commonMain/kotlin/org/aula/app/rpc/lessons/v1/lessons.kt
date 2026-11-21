@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.lessons.v1

@pbandk.Export
public sealed class PulseState(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.lessons.v1.PulseState && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "PulseState.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : PulseState(0, "PULSE_STATE_UNSPECIFIED")
    public object OK : PulseState(1, "PULSE_STATE_OK")
    public object LOST : PulseState(2, "PULSE_STATE_LOST")
    public class UNRECOGNIZED(value: Int) : PulseState(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.lessons.v1.PulseState> {
        public val values: List<org.aula.app.rpc.lessons.v1.PulseState> by lazy { listOf(UNSPECIFIED, OK, LOST) }
        override fun fromValue(value: Int): org.aula.app.rpc.lessons.v1.PulseState = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.lessons.v1.PulseState = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No PulseState with name: $name")
    }
}

@pbandk.Export
public sealed class MarkerKind(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.lessons.v1.MarkerKind && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "MarkerKind.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : MarkerKind(0, "MARKER_KIND_UNSPECIFIED")
    public object QUIZ : MarkerKind(1, "MARKER_KIND_QUIZ")
    public object DOUBTS : MarkerKind(2, "MARKER_KIND_DOUBTS")
    public class UNRECOGNIZED(value: Int) : MarkerKind(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.lessons.v1.MarkerKind> {
        public val values: List<org.aula.app.rpc.lessons.v1.MarkerKind> by lazy { listOf(UNSPECIFIED, QUIZ, DOUBTS) }
        override fun fromValue(value: Int): org.aula.app.rpc.lessons.v1.MarkerKind = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.lessons.v1.MarkerKind = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No MarkerKind with name: $name")
    }
}

@pbandk.Export
public data class Pulse(
    val okCount: Int = 0,
    val lostCount: Int = 0,
    val okPercent: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.Pulse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.Pulse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.Pulse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.Pulse by lazy { org.aula.app.rpc.lessons.v1.Pulse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.Pulse = org.aula.app.rpc.lessons.v1.Pulse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.Pulse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.Pulse",
            messageClass = org.aula.app.rpc.lessons.v1.Pulse::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ok_count",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "okCount",
                        value = org.aula.app.rpc.lessons.v1.Pulse::okCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lost_count",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "lostCount",
                        value = org.aula.app.rpc.lessons.v1.Pulse::lostCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ok_percent",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "okPercent",
                        value = org.aula.app.rpc.lessons.v1.Pulse::okPercent
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class DoubtRoundRef(
    val id: String = "",
    val closesAt: pbandk.wkt.Timestamp? = null,
    val submittedCount: Int = 0,
    val mySubmitted: Boolean = false,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.DoubtRoundRef = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.DoubtRoundRef> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.DoubtRoundRef> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.DoubtRoundRef by lazy { org.aula.app.rpc.lessons.v1.DoubtRoundRef() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.DoubtRoundRef = org.aula.app.rpc.lessons.v1.DoubtRoundRef.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.DoubtRoundRef> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.DoubtRoundRef",
            messageClass = org.aula.app.rpc.lessons.v1.DoubtRoundRef::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.DoubtRoundRef::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "closes_at",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "closesAt",
                        value = org.aula.app.rpc.lessons.v1.DoubtRoundRef::closesAt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "submitted_count",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "submittedCount",
                        value = org.aula.app.rpc.lessons.v1.DoubtRoundRef::submittedCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "my_submitted",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "mySubmitted",
                        value = org.aula.app.rpc.lessons.v1.DoubtRoundRef::mySubmitted
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class FloorRoundRef(
    val id: String = "",
    val prompt: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.FloorRoundRef = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.FloorRoundRef> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.FloorRoundRef> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.FloorRoundRef by lazy { org.aula.app.rpc.lessons.v1.FloorRoundRef() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.FloorRoundRef = org.aula.app.rpc.lessons.v1.FloorRoundRef.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.FloorRoundRef> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.FloorRoundRef",
            messageClass = org.aula.app.rpc.lessons.v1.FloorRoundRef::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.FloorRoundRef::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "prompt",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "prompt",
                        value = org.aula.app.rpc.lessons.v1.FloorRoundRef::prompt
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class QuizRef(
    val id: String = "",
    val closesAt: pbandk.wkt.Timestamp? = null,
    val answeredCount: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.QuizRef = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.QuizRef> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.QuizRef> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.QuizRef by lazy { org.aula.app.rpc.lessons.v1.QuizRef() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.QuizRef = org.aula.app.rpc.lessons.v1.QuizRef.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.QuizRef> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.QuizRef",
            messageClass = org.aula.app.rpc.lessons.v1.QuizRef::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.QuizRef::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "closes_at",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "closesAt",
                        value = org.aula.app.rpc.lessons.v1.QuizRef::closesAt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "answered_count",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "answeredCount",
                        value = org.aula.app.rpc.lessons.v1.QuizRef::answeredCount
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class AttendanceRoundRef(
    val id: String = "",
    val presentCount: Int = 0,
    val needsReviewCount: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.AttendanceRoundRef = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.AttendanceRoundRef> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.AttendanceRoundRef> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.AttendanceRoundRef by lazy { org.aula.app.rpc.lessons.v1.AttendanceRoundRef() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.AttendanceRoundRef = org.aula.app.rpc.lessons.v1.AttendanceRoundRef.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.AttendanceRoundRef> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.AttendanceRoundRef",
            messageClass = org.aula.app.rpc.lessons.v1.AttendanceRoundRef::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.AttendanceRoundRef::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "present_count",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "presentCount",
                        value = org.aula.app.rpc.lessons.v1.AttendanceRoundRef::presentCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "needs_review_count",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "needsReviewCount",
                        value = org.aula.app.rpc.lessons.v1.AttendanceRoundRef::needsReviewCount
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LessonSnapshot(
    val serverTime: pbandk.wkt.Timestamp? = null,
    val lessonId: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val number: Int = 0,
    val status: org.aula.app.rpc.courses.v1.LessonStatus = org.aula.app.rpc.courses.v1.LessonStatus.fromValue(0),
    val startedAt: pbandk.wkt.Timestamp? = null,
    val enrolledCount: Int = 0,
    val joinedCount: Int = 0,
    val connectedCount: Int = 0,
    val pulse: org.aula.app.rpc.lessons.v1.Pulse? = null,
    val myPulse: org.aula.app.rpc.lessons.v1.PulseState = org.aula.app.rpc.lessons.v1.PulseState.fromValue(0),
    val activeDoubtRound: org.aula.app.rpc.lessons.v1.DoubtRoundRef? = null,
    val activeFloorRound: org.aula.app.rpc.lessons.v1.FloorRoundRef? = null,
    val activeQuiz: org.aula.app.rpc.lessons.v1.QuizRef? = null,
    val activeAttendanceRound: org.aula.app.rpc.lessons.v1.AttendanceRoundRef? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.LessonSnapshot = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.LessonSnapshot> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.LessonSnapshot> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.LessonSnapshot by lazy { org.aula.app.rpc.lessons.v1.LessonSnapshot() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.LessonSnapshot = org.aula.app.rpc.lessons.v1.LessonSnapshot.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.LessonSnapshot> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.LessonSnapshot",
            messageClass = org.aula.app.rpc.lessons.v1.LessonSnapshot::class,
            messageCompanion = this,
            fields = buildList(16) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "server_time",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "serverTime",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::serverTime
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_id",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseId",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::courseId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_name",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseName",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::courseName
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "number",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "number",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::number
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.courses.v1.LessonStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::status
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "started_at",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "startedAt",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::startedAt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "enrolled_count",
                        number = 8,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "enrolledCount",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::enrolledCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "joined_count",
                        number = 9,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "joinedCount",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::joinedCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "connected_count",
                        number = 10,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "connectedCount",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::connectedCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "pulse",
                        number = 11,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Pulse.Companion),
                        jsonName = "pulse",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::pulse
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "my_pulse",
                        number = 12,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.lessons.v1.PulseState.Companion),
                        jsonName = "myPulse",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::myPulse
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "active_doubt_round",
                        number = 13,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.DoubtRoundRef.Companion),
                        jsonName = "activeDoubtRound",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::activeDoubtRound
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "active_floor_round",
                        number = 14,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.FloorRoundRef.Companion),
                        jsonName = "activeFloorRound",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::activeFloorRound
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "active_quiz",
                        number = 15,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.QuizRef.Companion),
                        jsonName = "activeQuiz",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::activeQuiz
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "active_attendance_round",
                        number = 16,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.AttendanceRoundRef.Companion),
                        jsonName = "activeAttendanceRound",
                        value = org.aula.app.rpc.lessons.v1.LessonSnapshot::activeAttendanceRound
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class Lesson(
    val id: String = "",
    val courseId: String = "",
    val number: Int = 0,
    val status: org.aula.app.rpc.courses.v1.LessonStatus = org.aula.app.rpc.courses.v1.LessonStatus.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.Lesson = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.Lesson> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.Lesson> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.Lesson by lazy { org.aula.app.rpc.lessons.v1.Lesson() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.Lesson = org.aula.app.rpc.lessons.v1.Lesson.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.Lesson> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.Lesson",
            messageClass = org.aula.app.rpc.lessons.v1.Lesson::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.Lesson::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseId",
                        value = org.aula.app.rpc.lessons.v1.Lesson::courseId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "number",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "number",
                        value = org.aula.app.rpc.lessons.v1.Lesson::number
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.courses.v1.LessonStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.lessons.v1.Lesson::status
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenRoomRequest(
    val courseId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.OpenRoomRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.OpenRoomRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.OpenRoomRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.OpenRoomRequest by lazy { org.aula.app.rpc.lessons.v1.OpenRoomRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.OpenRoomRequest = org.aula.app.rpc.lessons.v1.OpenRoomRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.OpenRoomRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.OpenRoomRequest",
            messageClass = org.aula.app.rpc.lessons.v1.OpenRoomRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.OpenRoomRequest::courseId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenRoomResponse(
    val lesson: org.aula.app.rpc.lessons.v1.Lesson? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.OpenRoomResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.OpenRoomResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.OpenRoomResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.OpenRoomResponse by lazy { org.aula.app.rpc.lessons.v1.OpenRoomResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.OpenRoomResponse = org.aula.app.rpc.lessons.v1.OpenRoomResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.OpenRoomResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.OpenRoomResponse",
            messageClass = org.aula.app.rpc.lessons.v1.OpenRoomResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Lesson.Companion),
                        jsonName = "lesson",
                        value = org.aula.app.rpc.lessons.v1.OpenRoomResponse::lesson
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseRoomRequest(
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.CloseRoomRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.CloseRoomRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.CloseRoomRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.CloseRoomRequest by lazy { org.aula.app.rpc.lessons.v1.CloseRoomRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.CloseRoomRequest = org.aula.app.rpc.lessons.v1.CloseRoomRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.CloseRoomRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.CloseRoomRequest",
            messageClass = org.aula.app.rpc.lessons.v1.CloseRoomRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.CloseRoomRequest::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseRoomResponse(
    val lesson: org.aula.app.rpc.lessons.v1.Lesson? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.CloseRoomResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.CloseRoomResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.CloseRoomResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.CloseRoomResponse by lazy { org.aula.app.rpc.lessons.v1.CloseRoomResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.CloseRoomResponse = org.aula.app.rpc.lessons.v1.CloseRoomResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.CloseRoomResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.CloseRoomResponse",
            messageClass = org.aula.app.rpc.lessons.v1.CloseRoomResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Lesson.Companion),
                        jsonName = "lesson",
                        value = org.aula.app.rpc.lessons.v1.CloseRoomResponse::lesson
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class StartLessonRequest(
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.StartLessonRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.StartLessonRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.StartLessonRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.StartLessonRequest by lazy { org.aula.app.rpc.lessons.v1.StartLessonRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.StartLessonRequest = org.aula.app.rpc.lessons.v1.StartLessonRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.StartLessonRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.StartLessonRequest",
            messageClass = org.aula.app.rpc.lessons.v1.StartLessonRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.StartLessonRequest::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class StartLessonResponse(
    val lesson: org.aula.app.rpc.lessons.v1.Lesson? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.StartLessonResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.StartLessonResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.StartLessonResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.StartLessonResponse by lazy { org.aula.app.rpc.lessons.v1.StartLessonResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.StartLessonResponse = org.aula.app.rpc.lessons.v1.StartLessonResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.StartLessonResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.StartLessonResponse",
            messageClass = org.aula.app.rpc.lessons.v1.StartLessonResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Lesson.Companion),
                        jsonName = "lesson",
                        value = org.aula.app.rpc.lessons.v1.StartLessonResponse::lesson
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EndLessonRequest(
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.EndLessonRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.EndLessonRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.EndLessonRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.EndLessonRequest by lazy { org.aula.app.rpc.lessons.v1.EndLessonRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.EndLessonRequest = org.aula.app.rpc.lessons.v1.EndLessonRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.EndLessonRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.EndLessonRequest",
            messageClass = org.aula.app.rpc.lessons.v1.EndLessonRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.EndLessonRequest::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EndLessonResponse(
    val lesson: org.aula.app.rpc.lessons.v1.Lesson? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.EndLessonResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.EndLessonResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.EndLessonResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.EndLessonResponse by lazy { org.aula.app.rpc.lessons.v1.EndLessonResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.EndLessonResponse = org.aula.app.rpc.lessons.v1.EndLessonResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.EndLessonResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.EndLessonResponse",
            messageClass = org.aula.app.rpc.lessons.v1.EndLessonResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Lesson.Companion),
                        jsonName = "lesson",
                        value = org.aula.app.rpc.lessons.v1.EndLessonResponse::lesson
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class JoinRoomRequest(
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.JoinRoomRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.JoinRoomRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.JoinRoomRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.JoinRoomRequest by lazy { org.aula.app.rpc.lessons.v1.JoinRoomRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.JoinRoomRequest = org.aula.app.rpc.lessons.v1.JoinRoomRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.JoinRoomRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.JoinRoomRequest",
            messageClass = org.aula.app.rpc.lessons.v1.JoinRoomRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.JoinRoomRequest::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class JoinRoomResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.JoinRoomResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.JoinRoomResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.JoinRoomResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.JoinRoomResponse by lazy { org.aula.app.rpc.lessons.v1.JoinRoomResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.JoinRoomResponse = org.aula.app.rpc.lessons.v1.JoinRoomResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.JoinRoomResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.JoinRoomResponse",
            messageClass = org.aula.app.rpc.lessons.v1.JoinRoomResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class GetLessonSnapshotRequest(
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest by lazy { org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest = org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.GetLessonSnapshotRequest",
            messageClass = org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetLessonSnapshotResponse(
    val snapshot: org.aula.app.rpc.lessons.v1.LessonSnapshot? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse by lazy { org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse = org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.GetLessonSnapshotResponse",
            messageClass = org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "snapshot",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.LessonSnapshot.Companion),
                        jsonName = "snapshot",
                        value = org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse::snapshot
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RealtimeHello(
    val accessToken: String = "",
    val schoolId: String = "",
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.RealtimeHello = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RealtimeHello> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.RealtimeHello> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.RealtimeHello by lazy { org.aula.app.rpc.lessons.v1.RealtimeHello() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.RealtimeHello = org.aula.app.rpc.lessons.v1.RealtimeHello.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RealtimeHello> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.RealtimeHello",
            messageClass = org.aula.app.rpc.lessons.v1.RealtimeHello::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "access_token",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "accessToken",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(4, 114, 2, 16, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.RealtimeHello::accessToken
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "school_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "schoolId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.RealtimeHello::schoolId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.RealtimeHello::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class WatchLessonRequest(
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.WatchLessonRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.WatchLessonRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.WatchLessonRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.WatchLessonRequest by lazy { org.aula.app.rpc.lessons.v1.WatchLessonRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.WatchLessonRequest = org.aula.app.rpc.lessons.v1.WatchLessonRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.WatchLessonRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.WatchLessonRequest",
            messageClass = org.aula.app.rpc.lessons.v1.WatchLessonRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.WatchLessonRequest::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LessonStatusChanged(
    val status: org.aula.app.rpc.courses.v1.LessonStatus = org.aula.app.rpc.courses.v1.LessonStatus.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.LessonStatusChanged = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.LessonStatusChanged> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.LessonStatusChanged> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.LessonStatusChanged by lazy { org.aula.app.rpc.lessons.v1.LessonStatusChanged() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.LessonStatusChanged = org.aula.app.rpc.lessons.v1.LessonStatusChanged.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.LessonStatusChanged> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.LessonStatusChanged",
            messageClass = org.aula.app.rpc.lessons.v1.LessonStatusChanged::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.courses.v1.LessonStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.lessons.v1.LessonStatusChanged::status
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class PresenceChanged(
    val joinedCount: Int = 0,
    val connectedCount: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.PresenceChanged = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.PresenceChanged> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.PresenceChanged> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.PresenceChanged by lazy { org.aula.app.rpc.lessons.v1.PresenceChanged() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.PresenceChanged = org.aula.app.rpc.lessons.v1.PresenceChanged.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.PresenceChanged> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.PresenceChanged",
            messageClass = org.aula.app.rpc.lessons.v1.PresenceChanged::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "joined_count",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "joinedCount",
                        value = org.aula.app.rpc.lessons.v1.PresenceChanged::joinedCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "connected_count",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "connectedCount",
                        value = org.aula.app.rpc.lessons.v1.PresenceChanged::connectedCount
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class PulseChanged(
    val pulse: org.aula.app.rpc.lessons.v1.Pulse? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.PulseChanged = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.PulseChanged> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.PulseChanged> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.PulseChanged by lazy { org.aula.app.rpc.lessons.v1.PulseChanged() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.PulseChanged = org.aula.app.rpc.lessons.v1.PulseChanged.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.PulseChanged> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.PulseChanged",
            messageClass = org.aula.app.rpc.lessons.v1.PulseChanged::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "pulse",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Pulse.Companion),
                        jsonName = "pulse",
                        value = org.aula.app.rpc.lessons.v1.PulseChanged::pulse
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RoundOpened(
    val id: String = "",
    val closesAt: pbandk.wkt.Timestamp? = null,
    val prompt: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.RoundOpened = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RoundOpened> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.RoundOpened> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.RoundOpened by lazy { org.aula.app.rpc.lessons.v1.RoundOpened() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.RoundOpened = org.aula.app.rpc.lessons.v1.RoundOpened.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RoundOpened> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.RoundOpened",
            messageClass = org.aula.app.rpc.lessons.v1.RoundOpened::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.RoundOpened::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "closes_at",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "closesAt",
                        value = org.aula.app.rpc.lessons.v1.RoundOpened::closesAt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "prompt",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "prompt",
                        value = org.aula.app.rpc.lessons.v1.RoundOpened::prompt
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RoundChanged(
    val id: String = "",
    val closesAt: pbandk.wkt.Timestamp? = null,
    val count: Int = 0,
    val count2: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.RoundChanged = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RoundChanged> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.RoundChanged> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.RoundChanged by lazy { org.aula.app.rpc.lessons.v1.RoundChanged() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.RoundChanged = org.aula.app.rpc.lessons.v1.RoundChanged.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RoundChanged> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.RoundChanged",
            messageClass = org.aula.app.rpc.lessons.v1.RoundChanged::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.RoundChanged::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "closes_at",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "closesAt",
                        value = org.aula.app.rpc.lessons.v1.RoundChanged::closesAt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "count",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "count",
                        value = org.aula.app.rpc.lessons.v1.RoundChanged::count
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "count2",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "count2",
                        value = org.aula.app.rpc.lessons.v1.RoundChanged::count2
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RoundClosed(
    val id: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.RoundClosed = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RoundClosed> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.RoundClosed> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.RoundClosed by lazy { org.aula.app.rpc.lessons.v1.RoundClosed() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.RoundClosed = org.aula.app.rpc.lessons.v1.RoundClosed.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RoundClosed> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.RoundClosed",
            messageClass = org.aula.app.rpc.lessons.v1.RoundClosed::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.RoundClosed::id
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RoundSummaryReady(
    val id: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.RoundSummaryReady = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RoundSummaryReady> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.RoundSummaryReady> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.RoundSummaryReady by lazy { org.aula.app.rpc.lessons.v1.RoundSummaryReady() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.RoundSummaryReady = org.aula.app.rpc.lessons.v1.RoundSummaryReady.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.RoundSummaryReady> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.RoundSummaryReady",
            messageClass = org.aula.app.rpc.lessons.v1.RoundSummaryReady::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.RoundSummaryReady::id
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LessonEvent(
    val event: Event<*>? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    public sealed class Event<V>(value: V) : pbandk.Message.OneOf<V>(value) {
        public class Snapshot(snapshot: org.aula.app.rpc.lessons.v1.LessonSnapshot) : Event<org.aula.app.rpc.lessons.v1.LessonSnapshot>(snapshot)
        public class LessonStatus(lessonStatus: org.aula.app.rpc.lessons.v1.LessonStatusChanged) : Event<org.aula.app.rpc.lessons.v1.LessonStatusChanged>(lessonStatus)
        public class Presence(presence: org.aula.app.rpc.lessons.v1.PresenceChanged) : Event<org.aula.app.rpc.lessons.v1.PresenceChanged>(presence)
        public class Pulse(pulse: org.aula.app.rpc.lessons.v1.PulseChanged) : Event<org.aula.app.rpc.lessons.v1.PulseChanged>(pulse)
        public class DoubtRoundOpened(doubtRoundOpened: org.aula.app.rpc.lessons.v1.RoundOpened) : Event<org.aula.app.rpc.lessons.v1.RoundOpened>(doubtRoundOpened)
        public class DoubtRoundChanged(doubtRoundChanged: org.aula.app.rpc.lessons.v1.RoundChanged) : Event<org.aula.app.rpc.lessons.v1.RoundChanged>(doubtRoundChanged)
        public class DoubtRoundClosed(doubtRoundClosed: org.aula.app.rpc.lessons.v1.RoundClosed) : Event<org.aula.app.rpc.lessons.v1.RoundClosed>(doubtRoundClosed)
        public class DoubtRoundSummaryReady(doubtRoundSummaryReady: org.aula.app.rpc.lessons.v1.RoundSummaryReady) : Event<org.aula.app.rpc.lessons.v1.RoundSummaryReady>(doubtRoundSummaryReady)
        public class FloorOpened(floorOpened: org.aula.app.rpc.lessons.v1.RoundOpened) : Event<org.aula.app.rpc.lessons.v1.RoundOpened>(floorOpened)
        public class FloorChanged(floorChanged: org.aula.app.rpc.lessons.v1.RoundChanged) : Event<org.aula.app.rpc.lessons.v1.RoundChanged>(floorChanged)
        public class FloorClosed(floorClosed: org.aula.app.rpc.lessons.v1.RoundClosed) : Event<org.aula.app.rpc.lessons.v1.RoundClosed>(floorClosed)
        public class QuizLaunched(quizLaunched: org.aula.app.rpc.lessons.v1.RoundOpened) : Event<org.aula.app.rpc.lessons.v1.RoundOpened>(quizLaunched)
        public class QuizProgress(quizProgress: org.aula.app.rpc.lessons.v1.RoundChanged) : Event<org.aula.app.rpc.lessons.v1.RoundChanged>(quizProgress)
        public class QuizClosed(quizClosed: org.aula.app.rpc.lessons.v1.RoundClosed) : Event<org.aula.app.rpc.lessons.v1.RoundClosed>(quizClosed)
        public class AttendanceOpened(attendanceOpened: org.aula.app.rpc.lessons.v1.RoundOpened) : Event<org.aula.app.rpc.lessons.v1.RoundOpened>(attendanceOpened)
        public class AttendanceProgress(attendanceProgress: org.aula.app.rpc.lessons.v1.RoundChanged) : Event<org.aula.app.rpc.lessons.v1.RoundChanged>(attendanceProgress)
        public class AttendanceClosed(attendanceClosed: org.aula.app.rpc.lessons.v1.RoundClosed) : Event<org.aula.app.rpc.lessons.v1.RoundClosed>(attendanceClosed)
    }

    val snapshot: org.aula.app.rpc.lessons.v1.LessonSnapshot?
        get() = (event as? Event.Snapshot)?.value
    val lessonStatus: org.aula.app.rpc.lessons.v1.LessonStatusChanged?
        get() = (event as? Event.LessonStatus)?.value
    val presence: org.aula.app.rpc.lessons.v1.PresenceChanged?
        get() = (event as? Event.Presence)?.value
    val pulse: org.aula.app.rpc.lessons.v1.PulseChanged?
        get() = (event as? Event.Pulse)?.value
    val doubtRoundOpened: org.aula.app.rpc.lessons.v1.RoundOpened?
        get() = (event as? Event.DoubtRoundOpened)?.value
    val doubtRoundChanged: org.aula.app.rpc.lessons.v1.RoundChanged?
        get() = (event as? Event.DoubtRoundChanged)?.value
    val doubtRoundClosed: org.aula.app.rpc.lessons.v1.RoundClosed?
        get() = (event as? Event.DoubtRoundClosed)?.value
    val doubtRoundSummaryReady: org.aula.app.rpc.lessons.v1.RoundSummaryReady?
        get() = (event as? Event.DoubtRoundSummaryReady)?.value
    val floorOpened: org.aula.app.rpc.lessons.v1.RoundOpened?
        get() = (event as? Event.FloorOpened)?.value
    val floorChanged: org.aula.app.rpc.lessons.v1.RoundChanged?
        get() = (event as? Event.FloorChanged)?.value
    val floorClosed: org.aula.app.rpc.lessons.v1.RoundClosed?
        get() = (event as? Event.FloorClosed)?.value
    val quizLaunched: org.aula.app.rpc.lessons.v1.RoundOpened?
        get() = (event as? Event.QuizLaunched)?.value
    val quizProgress: org.aula.app.rpc.lessons.v1.RoundChanged?
        get() = (event as? Event.QuizProgress)?.value
    val quizClosed: org.aula.app.rpc.lessons.v1.RoundClosed?
        get() = (event as? Event.QuizClosed)?.value
    val attendanceOpened: org.aula.app.rpc.lessons.v1.RoundOpened?
        get() = (event as? Event.AttendanceOpened)?.value
    val attendanceProgress: org.aula.app.rpc.lessons.v1.RoundChanged?
        get() = (event as? Event.AttendanceProgress)?.value
    val attendanceClosed: org.aula.app.rpc.lessons.v1.RoundClosed?
        get() = (event as? Event.AttendanceClosed)?.value

    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.LessonEvent = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.LessonEvent> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.LessonEvent> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.LessonEvent by lazy { org.aula.app.rpc.lessons.v1.LessonEvent() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.LessonEvent = org.aula.app.rpc.lessons.v1.LessonEvent.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.LessonEvent> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.LessonEvent",
            messageClass = org.aula.app.rpc.lessons.v1.LessonEvent::class,
            messageCompanion = this,
            fields = buildList(17) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "snapshot",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.LessonSnapshot.Companion),
                        oneofMember = true,
                        jsonName = "snapshot",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::snapshot
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_status",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.LessonStatusChanged.Companion),
                        oneofMember = true,
                        jsonName = "lessonStatus",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::lessonStatus
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "presence",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.PresenceChanged.Companion),
                        oneofMember = true,
                        jsonName = "presence",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::presence
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "pulse",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.PulseChanged.Companion),
                        oneofMember = true,
                        jsonName = "pulse",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::pulse
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "doubt_round_opened",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundOpened.Companion),
                        oneofMember = true,
                        jsonName = "doubtRoundOpened",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::doubtRoundOpened
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "doubt_round_changed",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundChanged.Companion),
                        oneofMember = true,
                        jsonName = "doubtRoundChanged",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::doubtRoundChanged
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "doubt_round_closed",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundClosed.Companion),
                        oneofMember = true,
                        jsonName = "doubtRoundClosed",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::doubtRoundClosed
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "doubt_round_summary_ready",
                        number = 8,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundSummaryReady.Companion),
                        oneofMember = true,
                        jsonName = "doubtRoundSummaryReady",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::doubtRoundSummaryReady
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "floor_opened",
                        number = 9,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundOpened.Companion),
                        oneofMember = true,
                        jsonName = "floorOpened",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::floorOpened
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "floor_changed",
                        number = 10,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundChanged.Companion),
                        oneofMember = true,
                        jsonName = "floorChanged",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::floorChanged
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "floor_closed",
                        number = 11,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundClosed.Companion),
                        oneofMember = true,
                        jsonName = "floorClosed",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::floorClosed
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_launched",
                        number = 12,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundOpened.Companion),
                        oneofMember = true,
                        jsonName = "quizLaunched",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::quizLaunched
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_progress",
                        number = 13,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundChanged.Companion),
                        oneofMember = true,
                        jsonName = "quizProgress",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::quizProgress
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_closed",
                        number = 14,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundClosed.Companion),
                        oneofMember = true,
                        jsonName = "quizClosed",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::quizClosed
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "attendance_opened",
                        number = 15,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundOpened.Companion),
                        oneofMember = true,
                        jsonName = "attendanceOpened",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::attendanceOpened
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "attendance_progress",
                        number = 16,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundChanged.Companion),
                        oneofMember = true,
                        jsonName = "attendanceProgress",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::attendanceProgress
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "attendance_closed",
                        number = 17,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.RoundClosed.Companion),
                        oneofMember = true,
                        jsonName = "attendanceClosed",
                        value = org.aula.app.rpc.lessons.v1.LessonEvent::attendanceClosed
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class SetPulseRequest(
    val lessonId: String = "",
    val state: org.aula.app.rpc.lessons.v1.PulseState = org.aula.app.rpc.lessons.v1.PulseState.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.SetPulseRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.SetPulseRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.SetPulseRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.SetPulseRequest by lazy { org.aula.app.rpc.lessons.v1.SetPulseRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.SetPulseRequest = org.aula.app.rpc.lessons.v1.SetPulseRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.SetPulseRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.SetPulseRequest",
            messageClass = org.aula.app.rpc.lessons.v1.SetPulseRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.SetPulseRequest::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "state",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.lessons.v1.PulseState.Companion),
                        jsonName = "state",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, -126, 1, 4, 16, 1, 32, 0))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.SetPulseRequest::state
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class SetPulseResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.SetPulseResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.SetPulseResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.SetPulseResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.SetPulseResponse by lazy { org.aula.app.rpc.lessons.v1.SetPulseResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.SetPulseResponse = org.aula.app.rpc.lessons.v1.SetPulseResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.SetPulseResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.SetPulseResponse",
            messageClass = org.aula.app.rpc.lessons.v1.SetPulseResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class PulsePoint(
    val minute: Int = 0,
    val okPercent: Int = 0,
    val lostCount: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.PulsePoint = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.PulsePoint> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.PulsePoint> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.PulsePoint by lazy { org.aula.app.rpc.lessons.v1.PulsePoint() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.PulsePoint = org.aula.app.rpc.lessons.v1.PulsePoint.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.PulsePoint> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.PulsePoint",
            messageClass = org.aula.app.rpc.lessons.v1.PulsePoint::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "minute",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "minute",
                        value = org.aula.app.rpc.lessons.v1.PulsePoint::minute
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ok_percent",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "okPercent",
                        value = org.aula.app.rpc.lessons.v1.PulsePoint::okPercent
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lost_count",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "lostCount",
                        value = org.aula.app.rpc.lessons.v1.PulsePoint::lostCount
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class Marker(
    val minute: Int = 0,
    val kind: org.aula.app.rpc.lessons.v1.MarkerKind = org.aula.app.rpc.lessons.v1.MarkerKind.fromValue(0),
    val refId: String = "",
    val label: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.Marker = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.Marker> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.Marker> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.Marker by lazy { org.aula.app.rpc.lessons.v1.Marker() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.Marker = org.aula.app.rpc.lessons.v1.Marker.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.Marker> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.Marker",
            messageClass = org.aula.app.rpc.lessons.v1.Marker::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "minute",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "minute",
                        value = org.aula.app.rpc.lessons.v1.Marker::minute
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "kind",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.lessons.v1.MarkerKind.Companion),
                        jsonName = "kind",
                        value = org.aula.app.rpc.lessons.v1.Marker::kind
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ref_id",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "refId",
                        value = org.aula.app.rpc.lessons.v1.Marker::refId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "label",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "label",
                        value = org.aula.app.rpc.lessons.v1.Marker::label
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class PeakMoment(
    val minute: Int = 0,
    val lostCount: Int = 0,
    val topDoubt: String = "",
    val slideNumber: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.PeakMoment = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.PeakMoment> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.PeakMoment> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.PeakMoment by lazy { org.aula.app.rpc.lessons.v1.PeakMoment() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.PeakMoment = org.aula.app.rpc.lessons.v1.PeakMoment.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.PeakMoment> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.PeakMoment",
            messageClass = org.aula.app.rpc.lessons.v1.PeakMoment::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "minute",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "minute",
                        value = org.aula.app.rpc.lessons.v1.PeakMoment::minute
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lost_count",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "lostCount",
                        value = org.aula.app.rpc.lessons.v1.PeakMoment::lostCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "top_doubt",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "topDoubt",
                        value = org.aula.app.rpc.lessons.v1.PeakMoment::topDoubt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "slide_number",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "slideNumber",
                        value = org.aula.app.rpc.lessons.v1.PeakMoment::slideNumber
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class AttendanceTotals(
    val present: Int = 0,
    val needsReview: Int = 0,
    val absent: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.AttendanceTotals = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.AttendanceTotals> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.AttendanceTotals> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.AttendanceTotals by lazy { org.aula.app.rpc.lessons.v1.AttendanceTotals() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.AttendanceTotals = org.aula.app.rpc.lessons.v1.AttendanceTotals.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.AttendanceTotals> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.AttendanceTotals",
            messageClass = org.aula.app.rpc.lessons.v1.AttendanceTotals::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "present",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "present",
                        value = org.aula.app.rpc.lessons.v1.AttendanceTotals::present
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "needs_review",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "needsReview",
                        value = org.aula.app.rpc.lessons.v1.AttendanceTotals::needsReview
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "absent",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "absent",
                        value = org.aula.app.rpc.lessons.v1.AttendanceTotals::absent
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetLessonReviewRequest(
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.GetLessonReviewRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.GetLessonReviewRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.GetLessonReviewRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.GetLessonReviewRequest by lazy { org.aula.app.rpc.lessons.v1.GetLessonReviewRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.GetLessonReviewRequest = org.aula.app.rpc.lessons.v1.GetLessonReviewRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.GetLessonReviewRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.GetLessonReviewRequest",
            messageClass = org.aula.app.rpc.lessons.v1.GetLessonReviewRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewRequest::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetLessonReviewResponse(
    val lessonId: String = "",
    val courseId: String = "",
    val courseName: String = "",
    val number: Int = 0,
    val durationSeconds: Int = 0,
    val enrolledCount: Int = 0,
    val pulseSeries: List<org.aula.app.rpc.lessons.v1.PulsePoint> = emptyList(),
    val markers: List<org.aula.app.rpc.lessons.v1.Marker> = emptyList(),
    val peak: org.aula.app.rpc.lessons.v1.PeakMoment? = null,
    val attendance: org.aula.app.rpc.lessons.v1.AttendanceTotals? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.GetLessonReviewResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.GetLessonReviewResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.GetLessonReviewResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.GetLessonReviewResponse by lazy { org.aula.app.rpc.lessons.v1.GetLessonReviewResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.GetLessonReviewResponse = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.GetLessonReviewResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.GetLessonReviewResponse",
            messageClass = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::class,
            messageCompanion = this,
            fields = buildList(10) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseId",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::courseId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_name",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseName",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::courseName
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "number",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "number",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::number
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "duration_seconds",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "durationSeconds",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::durationSeconds
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "enrolled_count",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "enrolledCount",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::enrolledCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "pulse_series",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.lessons.v1.PulsePoint>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.PulsePoint.Companion)),
                        jsonName = "pulseSeries",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::pulseSeries
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "markers",
                        number = 8,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.lessons.v1.Marker>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Marker.Companion)),
                        jsonName = "markers",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::markers
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "peak",
                        number = 9,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.PeakMoment.Companion),
                        jsonName = "peak",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::peak
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "attendance",
                        number = 10,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.AttendanceTotals.Companion),
                        jsonName = "attendance",
                        value = org.aula.app.rpc.lessons.v1.GetLessonReviewResponse::attendance
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class Material(
    val id: String = "",
    val lessonId: String = "",
    val filename: String = "",
    val ready: Boolean = false,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.Material = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.Material> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.Material> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.Material by lazy { org.aula.app.rpc.lessons.v1.Material() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.Material = org.aula.app.rpc.lessons.v1.Material.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.Material> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.Material",
            messageClass = org.aula.app.rpc.lessons.v1.Material::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.lessons.v1.Material::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        value = org.aula.app.rpc.lessons.v1.Material::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "filename",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "filename",
                        value = org.aula.app.rpc.lessons.v1.Material::filename
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ready",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "ready",
                        value = org.aula.app.rpc.lessons.v1.Material::ready
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class UploadMaterialRequest(
    val lessonId: String = "",
    val filename: String = "",
    val content: pbandk.ByteArr = pbandk.ByteArr.empty,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.UploadMaterialRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.UploadMaterialRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.UploadMaterialRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.UploadMaterialRequest by lazy { org.aula.app.rpc.lessons.v1.UploadMaterialRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.UploadMaterialRequest = org.aula.app.rpc.lessons.v1.UploadMaterialRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.UploadMaterialRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.UploadMaterialRequest",
            messageClass = org.aula.app.rpc.lessons.v1.UploadMaterialRequest::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.UploadMaterialRequest::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "filename",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "filename",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 114, 5, 16, 1, 24, -56, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.UploadMaterialRequest::filename
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "content",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bytes(),
                        jsonName = "content",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(9, 122, 7, 16, 1, 24, -64, -61, -109, 7))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.UploadMaterialRequest::content
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class UploadMaterialResponse(
    val material: org.aula.app.rpc.lessons.v1.Material? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.UploadMaterialResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.UploadMaterialResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.UploadMaterialResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.UploadMaterialResponse by lazy { org.aula.app.rpc.lessons.v1.UploadMaterialResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.UploadMaterialResponse = org.aula.app.rpc.lessons.v1.UploadMaterialResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.UploadMaterialResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.UploadMaterialResponse",
            messageClass = org.aula.app.rpc.lessons.v1.UploadMaterialResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "material",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Material.Companion),
                        jsonName = "material",
                        value = org.aula.app.rpc.lessons.v1.UploadMaterialResponse::material
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ListMaterialsRequest(
    val lessonId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.ListMaterialsRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.ListMaterialsRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.ListMaterialsRequest> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.ListMaterialsRequest by lazy { org.aula.app.rpc.lessons.v1.ListMaterialsRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.ListMaterialsRequest = org.aula.app.rpc.lessons.v1.ListMaterialsRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.ListMaterialsRequest> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.ListMaterialsRequest",
            messageClass = org.aula.app.rpc.lessons.v1.ListMaterialsRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, -80, 1, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.lessons.v1.ListMaterialsRequest::lessonId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ListMaterialsResponse(
    val materials: List<org.aula.app.rpc.lessons.v1.Material> = emptyList(),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.lessons.v1.ListMaterialsResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.ListMaterialsResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.lessons.v1.ListMaterialsResponse> {
        public val defaultInstance: org.aula.app.rpc.lessons.v1.ListMaterialsResponse by lazy { org.aula.app.rpc.lessons.v1.ListMaterialsResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.lessons.v1.ListMaterialsResponse = org.aula.app.rpc.lessons.v1.ListMaterialsResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.lessons.v1.ListMaterialsResponse> = pbandk.MessageDescriptor(
            fullName = "aula.lessons.v1.ListMaterialsResponse",
            messageClass = org.aula.app.rpc.lessons.v1.ListMaterialsResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "materials",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.lessons.v1.Material>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.lessons.v1.Material.Companion)),
                        jsonName = "materials",
                        value = org.aula.app.rpc.lessons.v1.ListMaterialsResponse::materials
                    )
                )
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForPulse")
public fun Pulse?.orDefault(): org.aula.app.rpc.lessons.v1.Pulse = this ?: Pulse.defaultInstance

private fun Pulse.protoMergeImpl(plus: pbandk.Message?): Pulse = (plus as? Pulse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Pulse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Pulse {
    var okCount = 0
    var lostCount = 0
    var okPercent = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> okCount = _fieldValue as Int
            2 -> lostCount = _fieldValue as Int
            3 -> okPercent = _fieldValue as Int
        }
    }

    return Pulse(okCount, lostCount, okPercent, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForDoubtRoundRef")
public fun DoubtRoundRef?.orDefault(): org.aula.app.rpc.lessons.v1.DoubtRoundRef = this ?: DoubtRoundRef.defaultInstance

private fun DoubtRoundRef.protoMergeImpl(plus: pbandk.Message?): DoubtRoundRef = (plus as? DoubtRoundRef)?.let {
    it.copy(
        closesAt = closesAt?.plus(plus.closesAt) ?: plus.closesAt,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun DoubtRoundRef.Companion.decodeWithImpl(u: pbandk.MessageDecoder): DoubtRoundRef {
    var id = ""
    var closesAt: pbandk.wkt.Timestamp? = null
    var submittedCount = 0
    var mySubmitted = false

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> closesAt = _fieldValue as pbandk.wkt.Timestamp
            3 -> submittedCount = _fieldValue as Int
            4 -> mySubmitted = _fieldValue as Boolean
        }
    }

    return DoubtRoundRef(id, closesAt, submittedCount, mySubmitted, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForFloorRoundRef")
public fun FloorRoundRef?.orDefault(): org.aula.app.rpc.lessons.v1.FloorRoundRef = this ?: FloorRoundRef.defaultInstance

private fun FloorRoundRef.protoMergeImpl(plus: pbandk.Message?): FloorRoundRef = (plus as? FloorRoundRef)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun FloorRoundRef.Companion.decodeWithImpl(u: pbandk.MessageDecoder): FloorRoundRef {
    var id = ""
    var prompt = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> prompt = _fieldValue as String
        }
    }

    return FloorRoundRef(id, prompt, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForQuizRef")
public fun QuizRef?.orDefault(): org.aula.app.rpc.lessons.v1.QuizRef = this ?: QuizRef.defaultInstance

private fun QuizRef.protoMergeImpl(plus: pbandk.Message?): QuizRef = (plus as? QuizRef)?.let {
    it.copy(
        closesAt = closesAt?.plus(plus.closesAt) ?: plus.closesAt,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun QuizRef.Companion.decodeWithImpl(u: pbandk.MessageDecoder): QuizRef {
    var id = ""
    var closesAt: pbandk.wkt.Timestamp? = null
    var answeredCount = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> closesAt = _fieldValue as pbandk.wkt.Timestamp
            3 -> answeredCount = _fieldValue as Int
        }
    }

    return QuizRef(id, closesAt, answeredCount, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForAttendanceRoundRef")
public fun AttendanceRoundRef?.orDefault(): org.aula.app.rpc.lessons.v1.AttendanceRoundRef = this ?: AttendanceRoundRef.defaultInstance

private fun AttendanceRoundRef.protoMergeImpl(plus: pbandk.Message?): AttendanceRoundRef = (plus as? AttendanceRoundRef)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun AttendanceRoundRef.Companion.decodeWithImpl(u: pbandk.MessageDecoder): AttendanceRoundRef {
    var id = ""
    var presentCount = 0
    var needsReviewCount = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> presentCount = _fieldValue as Int
            3 -> needsReviewCount = _fieldValue as Int
        }
    }

    return AttendanceRoundRef(id, presentCount, needsReviewCount, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLessonSnapshot")
public fun LessonSnapshot?.orDefault(): org.aula.app.rpc.lessons.v1.LessonSnapshot = this ?: LessonSnapshot.defaultInstance

private fun LessonSnapshot.protoMergeImpl(plus: pbandk.Message?): LessonSnapshot = (plus as? LessonSnapshot)?.let {
    it.copy(
        serverTime = serverTime?.plus(plus.serverTime) ?: plus.serverTime,
        startedAt = startedAt?.plus(plus.startedAt) ?: plus.startedAt,
        pulse = pulse?.plus(plus.pulse) ?: plus.pulse,
        activeDoubtRound = activeDoubtRound?.plus(plus.activeDoubtRound) ?: plus.activeDoubtRound,
        activeFloorRound = activeFloorRound?.plus(plus.activeFloorRound) ?: plus.activeFloorRound,
        activeQuiz = activeQuiz?.plus(plus.activeQuiz) ?: plus.activeQuiz,
        activeAttendanceRound = activeAttendanceRound?.plus(plus.activeAttendanceRound) ?: plus.activeAttendanceRound,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LessonSnapshot.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LessonSnapshot {
    var serverTime: pbandk.wkt.Timestamp? = null
    var lessonId = ""
    var courseId = ""
    var courseName = ""
    var number = 0
    var status: org.aula.app.rpc.courses.v1.LessonStatus = org.aula.app.rpc.courses.v1.LessonStatus.fromValue(0)
    var startedAt: pbandk.wkt.Timestamp? = null
    var enrolledCount = 0
    var joinedCount = 0
    var connectedCount = 0
    var pulse: org.aula.app.rpc.lessons.v1.Pulse? = null
    var myPulse: org.aula.app.rpc.lessons.v1.PulseState = org.aula.app.rpc.lessons.v1.PulseState.fromValue(0)
    var activeDoubtRound: org.aula.app.rpc.lessons.v1.DoubtRoundRef? = null
    var activeFloorRound: org.aula.app.rpc.lessons.v1.FloorRoundRef? = null
    var activeQuiz: org.aula.app.rpc.lessons.v1.QuizRef? = null
    var activeAttendanceRound: org.aula.app.rpc.lessons.v1.AttendanceRoundRef? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> serverTime = _fieldValue as pbandk.wkt.Timestamp
            2 -> lessonId = _fieldValue as String
            3 -> courseId = _fieldValue as String
            4 -> courseName = _fieldValue as String
            5 -> number = _fieldValue as Int
            6 -> status = _fieldValue as org.aula.app.rpc.courses.v1.LessonStatus
            7 -> startedAt = _fieldValue as pbandk.wkt.Timestamp
            8 -> enrolledCount = _fieldValue as Int
            9 -> joinedCount = _fieldValue as Int
            10 -> connectedCount = _fieldValue as Int
            11 -> pulse = _fieldValue as org.aula.app.rpc.lessons.v1.Pulse
            12 -> myPulse = _fieldValue as org.aula.app.rpc.lessons.v1.PulseState
            13 -> activeDoubtRound = _fieldValue as org.aula.app.rpc.lessons.v1.DoubtRoundRef
            14 -> activeFloorRound = _fieldValue as org.aula.app.rpc.lessons.v1.FloorRoundRef
            15 -> activeQuiz = _fieldValue as org.aula.app.rpc.lessons.v1.QuizRef
            16 -> activeAttendanceRound = _fieldValue as org.aula.app.rpc.lessons.v1.AttendanceRoundRef
        }
    }

    return LessonSnapshot(serverTime, lessonId, courseId, courseName,
        number, status, startedAt, enrolledCount,
        joinedCount, connectedCount, pulse, myPulse,
        activeDoubtRound, activeFloorRound, activeQuiz, activeAttendanceRound, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLesson")
public fun Lesson?.orDefault(): org.aula.app.rpc.lessons.v1.Lesson = this ?: Lesson.defaultInstance

private fun Lesson.protoMergeImpl(plus: pbandk.Message?): Lesson = (plus as? Lesson)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Lesson.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Lesson {
    var id = ""
    var courseId = ""
    var number = 0
    var status: org.aula.app.rpc.courses.v1.LessonStatus = org.aula.app.rpc.courses.v1.LessonStatus.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> courseId = _fieldValue as String
            3 -> number = _fieldValue as Int
            4 -> status = _fieldValue as org.aula.app.rpc.courses.v1.LessonStatus
        }
    }

    return Lesson(id, courseId, number, status, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenRoomRequest")
public fun OpenRoomRequest?.orDefault(): org.aula.app.rpc.lessons.v1.OpenRoomRequest = this ?: OpenRoomRequest.defaultInstance

private fun OpenRoomRequest.protoMergeImpl(plus: pbandk.Message?): OpenRoomRequest = (plus as? OpenRoomRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenRoomRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenRoomRequest {
    var courseId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> courseId = _fieldValue as String
        }
    }

    return OpenRoomRequest(courseId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenRoomResponse")
public fun OpenRoomResponse?.orDefault(): org.aula.app.rpc.lessons.v1.OpenRoomResponse = this ?: OpenRoomResponse.defaultInstance

private fun OpenRoomResponse.protoMergeImpl(plus: pbandk.Message?): OpenRoomResponse = (plus as? OpenRoomResponse)?.let {
    it.copy(
        lesson = lesson?.plus(plus.lesson) ?: plus.lesson,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenRoomResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenRoomResponse {
    var lesson: org.aula.app.rpc.lessons.v1.Lesson? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lesson = _fieldValue as org.aula.app.rpc.lessons.v1.Lesson
        }
    }

    return OpenRoomResponse(lesson, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseRoomRequest")
public fun CloseRoomRequest?.orDefault(): org.aula.app.rpc.lessons.v1.CloseRoomRequest = this ?: CloseRoomRequest.defaultInstance

private fun CloseRoomRequest.protoMergeImpl(plus: pbandk.Message?): CloseRoomRequest = (plus as? CloseRoomRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseRoomRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseRoomRequest {
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
        }
    }

    return CloseRoomRequest(lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseRoomResponse")
public fun CloseRoomResponse?.orDefault(): org.aula.app.rpc.lessons.v1.CloseRoomResponse = this ?: CloseRoomResponse.defaultInstance

private fun CloseRoomResponse.protoMergeImpl(plus: pbandk.Message?): CloseRoomResponse = (plus as? CloseRoomResponse)?.let {
    it.copy(
        lesson = lesson?.plus(plus.lesson) ?: plus.lesson,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseRoomResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseRoomResponse {
    var lesson: org.aula.app.rpc.lessons.v1.Lesson? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lesson = _fieldValue as org.aula.app.rpc.lessons.v1.Lesson
        }
    }

    return CloseRoomResponse(lesson, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForStartLessonRequest")
public fun StartLessonRequest?.orDefault(): org.aula.app.rpc.lessons.v1.StartLessonRequest = this ?: StartLessonRequest.defaultInstance

private fun StartLessonRequest.protoMergeImpl(plus: pbandk.Message?): StartLessonRequest = (plus as? StartLessonRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun StartLessonRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): StartLessonRequest {
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
        }
    }

    return StartLessonRequest(lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForStartLessonResponse")
public fun StartLessonResponse?.orDefault(): org.aula.app.rpc.lessons.v1.StartLessonResponse = this ?: StartLessonResponse.defaultInstance

private fun StartLessonResponse.protoMergeImpl(plus: pbandk.Message?): StartLessonResponse = (plus as? StartLessonResponse)?.let {
    it.copy(
        lesson = lesson?.plus(plus.lesson) ?: plus.lesson,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun StartLessonResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): StartLessonResponse {
    var lesson: org.aula.app.rpc.lessons.v1.Lesson? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lesson = _fieldValue as org.aula.app.rpc.lessons.v1.Lesson
        }
    }

    return StartLessonResponse(lesson, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEndLessonRequest")
public fun EndLessonRequest?.orDefault(): org.aula.app.rpc.lessons.v1.EndLessonRequest = this ?: EndLessonRequest.defaultInstance

private fun EndLessonRequest.protoMergeImpl(plus: pbandk.Message?): EndLessonRequest = (plus as? EndLessonRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EndLessonRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EndLessonRequest {
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
        }
    }

    return EndLessonRequest(lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEndLessonResponse")
public fun EndLessonResponse?.orDefault(): org.aula.app.rpc.lessons.v1.EndLessonResponse = this ?: EndLessonResponse.defaultInstance

private fun EndLessonResponse.protoMergeImpl(plus: pbandk.Message?): EndLessonResponse = (plus as? EndLessonResponse)?.let {
    it.copy(
        lesson = lesson?.plus(plus.lesson) ?: plus.lesson,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EndLessonResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EndLessonResponse {
    var lesson: org.aula.app.rpc.lessons.v1.Lesson? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lesson = _fieldValue as org.aula.app.rpc.lessons.v1.Lesson
        }
    }

    return EndLessonResponse(lesson, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForJoinRoomRequest")
public fun JoinRoomRequest?.orDefault(): org.aula.app.rpc.lessons.v1.JoinRoomRequest = this ?: JoinRoomRequest.defaultInstance

private fun JoinRoomRequest.protoMergeImpl(plus: pbandk.Message?): JoinRoomRequest = (plus as? JoinRoomRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun JoinRoomRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): JoinRoomRequest {
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
        }
    }

    return JoinRoomRequest(lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForJoinRoomResponse")
public fun JoinRoomResponse?.orDefault(): org.aula.app.rpc.lessons.v1.JoinRoomResponse = this ?: JoinRoomResponse.defaultInstance

private fun JoinRoomResponse.protoMergeImpl(plus: pbandk.Message?): JoinRoomResponse = (plus as? JoinRoomResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun JoinRoomResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): JoinRoomResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return JoinRoomResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetLessonSnapshotRequest")
public fun GetLessonSnapshotRequest?.orDefault(): org.aula.app.rpc.lessons.v1.GetLessonSnapshotRequest = this ?: GetLessonSnapshotRequest.defaultInstance

private fun GetLessonSnapshotRequest.protoMergeImpl(plus: pbandk.Message?): GetLessonSnapshotRequest = (plus as? GetLessonSnapshotRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetLessonSnapshotRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetLessonSnapshotRequest {
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
        }
    }

    return GetLessonSnapshotRequest(lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetLessonSnapshotResponse")
public fun GetLessonSnapshotResponse?.orDefault(): org.aula.app.rpc.lessons.v1.GetLessonSnapshotResponse = this ?: GetLessonSnapshotResponse.defaultInstance

private fun GetLessonSnapshotResponse.protoMergeImpl(plus: pbandk.Message?): GetLessonSnapshotResponse = (plus as? GetLessonSnapshotResponse)?.let {
    it.copy(
        snapshot = snapshot?.plus(plus.snapshot) ?: plus.snapshot,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetLessonSnapshotResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetLessonSnapshotResponse {
    var snapshot: org.aula.app.rpc.lessons.v1.LessonSnapshot? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> snapshot = _fieldValue as org.aula.app.rpc.lessons.v1.LessonSnapshot
        }
    }

    return GetLessonSnapshotResponse(snapshot, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRealtimeHello")
public fun RealtimeHello?.orDefault(): org.aula.app.rpc.lessons.v1.RealtimeHello = this ?: RealtimeHello.defaultInstance

private fun RealtimeHello.protoMergeImpl(plus: pbandk.Message?): RealtimeHello = (plus as? RealtimeHello)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RealtimeHello.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RealtimeHello {
    var accessToken = ""
    var schoolId = ""
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> accessToken = _fieldValue as String
            2 -> schoolId = _fieldValue as String
            3 -> lessonId = _fieldValue as String
        }
    }

    return RealtimeHello(accessToken, schoolId, lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForWatchLessonRequest")
public fun WatchLessonRequest?.orDefault(): org.aula.app.rpc.lessons.v1.WatchLessonRequest = this ?: WatchLessonRequest.defaultInstance

private fun WatchLessonRequest.protoMergeImpl(plus: pbandk.Message?): WatchLessonRequest = (plus as? WatchLessonRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun WatchLessonRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): WatchLessonRequest {
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
        }
    }

    return WatchLessonRequest(lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLessonStatusChanged")
public fun LessonStatusChanged?.orDefault(): org.aula.app.rpc.lessons.v1.LessonStatusChanged = this ?: LessonStatusChanged.defaultInstance

private fun LessonStatusChanged.protoMergeImpl(plus: pbandk.Message?): LessonStatusChanged = (plus as? LessonStatusChanged)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LessonStatusChanged.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LessonStatusChanged {
    var status: org.aula.app.rpc.courses.v1.LessonStatus = org.aula.app.rpc.courses.v1.LessonStatus.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> status = _fieldValue as org.aula.app.rpc.courses.v1.LessonStatus
        }
    }

    return LessonStatusChanged(status, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForPresenceChanged")
public fun PresenceChanged?.orDefault(): org.aula.app.rpc.lessons.v1.PresenceChanged = this ?: PresenceChanged.defaultInstance

private fun PresenceChanged.protoMergeImpl(plus: pbandk.Message?): PresenceChanged = (plus as? PresenceChanged)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun PresenceChanged.Companion.decodeWithImpl(u: pbandk.MessageDecoder): PresenceChanged {
    var joinedCount = 0
    var connectedCount = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> joinedCount = _fieldValue as Int
            2 -> connectedCount = _fieldValue as Int
        }
    }

    return PresenceChanged(joinedCount, connectedCount, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForPulseChanged")
public fun PulseChanged?.orDefault(): org.aula.app.rpc.lessons.v1.PulseChanged = this ?: PulseChanged.defaultInstance

private fun PulseChanged.protoMergeImpl(plus: pbandk.Message?): PulseChanged = (plus as? PulseChanged)?.let {
    it.copy(
        pulse = pulse?.plus(plus.pulse) ?: plus.pulse,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun PulseChanged.Companion.decodeWithImpl(u: pbandk.MessageDecoder): PulseChanged {
    var pulse: org.aula.app.rpc.lessons.v1.Pulse? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> pulse = _fieldValue as org.aula.app.rpc.lessons.v1.Pulse
        }
    }

    return PulseChanged(pulse, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRoundOpened")
public fun RoundOpened?.orDefault(): org.aula.app.rpc.lessons.v1.RoundOpened = this ?: RoundOpened.defaultInstance

private fun RoundOpened.protoMergeImpl(plus: pbandk.Message?): RoundOpened = (plus as? RoundOpened)?.let {
    it.copy(
        closesAt = closesAt?.plus(plus.closesAt) ?: plus.closesAt,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RoundOpened.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RoundOpened {
    var id = ""
    var closesAt: pbandk.wkt.Timestamp? = null
    var prompt = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> closesAt = _fieldValue as pbandk.wkt.Timestamp
            3 -> prompt = _fieldValue as String
        }
    }

    return RoundOpened(id, closesAt, prompt, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRoundChanged")
public fun RoundChanged?.orDefault(): org.aula.app.rpc.lessons.v1.RoundChanged = this ?: RoundChanged.defaultInstance

private fun RoundChanged.protoMergeImpl(plus: pbandk.Message?): RoundChanged = (plus as? RoundChanged)?.let {
    it.copy(
        closesAt = closesAt?.plus(plus.closesAt) ?: plus.closesAt,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RoundChanged.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RoundChanged {
    var id = ""
    var closesAt: pbandk.wkt.Timestamp? = null
    var count = 0
    var count2 = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> closesAt = _fieldValue as pbandk.wkt.Timestamp
            3 -> count = _fieldValue as Int
            4 -> count2 = _fieldValue as Int
        }
    }

    return RoundChanged(id, closesAt, count, count2, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRoundClosed")
public fun RoundClosed?.orDefault(): org.aula.app.rpc.lessons.v1.RoundClosed = this ?: RoundClosed.defaultInstance

private fun RoundClosed.protoMergeImpl(plus: pbandk.Message?): RoundClosed = (plus as? RoundClosed)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RoundClosed.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RoundClosed {
    var id = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
        }
    }

    return RoundClosed(id, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRoundSummaryReady")
public fun RoundSummaryReady?.orDefault(): org.aula.app.rpc.lessons.v1.RoundSummaryReady = this ?: RoundSummaryReady.defaultInstance

private fun RoundSummaryReady.protoMergeImpl(plus: pbandk.Message?): RoundSummaryReady = (plus as? RoundSummaryReady)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RoundSummaryReady.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RoundSummaryReady {
    var id = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
        }
    }

    return RoundSummaryReady(id, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLessonEvent")
public fun LessonEvent?.orDefault(): org.aula.app.rpc.lessons.v1.LessonEvent = this ?: LessonEvent.defaultInstance

private fun LessonEvent.protoMergeImpl(plus: pbandk.Message?): LessonEvent = (plus as? LessonEvent)?.let {
    it.copy(
        event = when {
            event is LessonEvent.Event.Snapshot && plus.event is LessonEvent.Event.Snapshot ->
                LessonEvent.Event.Snapshot(event.value + plus.event.value)
            event is LessonEvent.Event.LessonStatus && plus.event is LessonEvent.Event.LessonStatus ->
                LessonEvent.Event.LessonStatus(event.value + plus.event.value)
            event is LessonEvent.Event.Presence && plus.event is LessonEvent.Event.Presence ->
                LessonEvent.Event.Presence(event.value + plus.event.value)
            event is LessonEvent.Event.Pulse && plus.event is LessonEvent.Event.Pulse ->
                LessonEvent.Event.Pulse(event.value + plus.event.value)
            event is LessonEvent.Event.DoubtRoundOpened && plus.event is LessonEvent.Event.DoubtRoundOpened ->
                LessonEvent.Event.DoubtRoundOpened(event.value + plus.event.value)
            event is LessonEvent.Event.DoubtRoundChanged && plus.event is LessonEvent.Event.DoubtRoundChanged ->
                LessonEvent.Event.DoubtRoundChanged(event.value + plus.event.value)
            event is LessonEvent.Event.DoubtRoundClosed && plus.event is LessonEvent.Event.DoubtRoundClosed ->
                LessonEvent.Event.DoubtRoundClosed(event.value + plus.event.value)
            event is LessonEvent.Event.DoubtRoundSummaryReady && plus.event is LessonEvent.Event.DoubtRoundSummaryReady ->
                LessonEvent.Event.DoubtRoundSummaryReady(event.value + plus.event.value)
            event is LessonEvent.Event.FloorOpened && plus.event is LessonEvent.Event.FloorOpened ->
                LessonEvent.Event.FloorOpened(event.value + plus.event.value)
            event is LessonEvent.Event.FloorChanged && plus.event is LessonEvent.Event.FloorChanged ->
                LessonEvent.Event.FloorChanged(event.value + plus.event.value)
            event is LessonEvent.Event.FloorClosed && plus.event is LessonEvent.Event.FloorClosed ->
                LessonEvent.Event.FloorClosed(event.value + plus.event.value)
            event is LessonEvent.Event.QuizLaunched && plus.event is LessonEvent.Event.QuizLaunched ->
                LessonEvent.Event.QuizLaunched(event.value + plus.event.value)
            event is LessonEvent.Event.QuizProgress && plus.event is LessonEvent.Event.QuizProgress ->
                LessonEvent.Event.QuizProgress(event.value + plus.event.value)
            event is LessonEvent.Event.QuizClosed && plus.event is LessonEvent.Event.QuizClosed ->
                LessonEvent.Event.QuizClosed(event.value + plus.event.value)
            event is LessonEvent.Event.AttendanceOpened && plus.event is LessonEvent.Event.AttendanceOpened ->
                LessonEvent.Event.AttendanceOpened(event.value + plus.event.value)
            event is LessonEvent.Event.AttendanceProgress && plus.event is LessonEvent.Event.AttendanceProgress ->
                LessonEvent.Event.AttendanceProgress(event.value + plus.event.value)
            event is LessonEvent.Event.AttendanceClosed && plus.event is LessonEvent.Event.AttendanceClosed ->
                LessonEvent.Event.AttendanceClosed(event.value + plus.event.value)
            else ->
                plus.event ?: event
        },
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LessonEvent.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LessonEvent {
    var event: LessonEvent.Event<*>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> event = LessonEvent.Event.Snapshot(_fieldValue as org.aula.app.rpc.lessons.v1.LessonSnapshot)
            2 -> event = LessonEvent.Event.LessonStatus(_fieldValue as org.aula.app.rpc.lessons.v1.LessonStatusChanged)
            3 -> event = LessonEvent.Event.Presence(_fieldValue as org.aula.app.rpc.lessons.v1.PresenceChanged)
            4 -> event = LessonEvent.Event.Pulse(_fieldValue as org.aula.app.rpc.lessons.v1.PulseChanged)
            5 -> event = LessonEvent.Event.DoubtRoundOpened(_fieldValue as org.aula.app.rpc.lessons.v1.RoundOpened)
            6 -> event = LessonEvent.Event.DoubtRoundChanged(_fieldValue as org.aula.app.rpc.lessons.v1.RoundChanged)
            7 -> event = LessonEvent.Event.DoubtRoundClosed(_fieldValue as org.aula.app.rpc.lessons.v1.RoundClosed)
            8 -> event = LessonEvent.Event.DoubtRoundSummaryReady(_fieldValue as org.aula.app.rpc.lessons.v1.RoundSummaryReady)
            9 -> event = LessonEvent.Event.FloorOpened(_fieldValue as org.aula.app.rpc.lessons.v1.RoundOpened)
            10 -> event = LessonEvent.Event.FloorChanged(_fieldValue as org.aula.app.rpc.lessons.v1.RoundChanged)
            11 -> event = LessonEvent.Event.FloorClosed(_fieldValue as org.aula.app.rpc.lessons.v1.RoundClosed)
            12 -> event = LessonEvent.Event.QuizLaunched(_fieldValue as org.aula.app.rpc.lessons.v1.RoundOpened)
            13 -> event = LessonEvent.Event.QuizProgress(_fieldValue as org.aula.app.rpc.lessons.v1.RoundChanged)
            14 -> event = LessonEvent.Event.QuizClosed(_fieldValue as org.aula.app.rpc.lessons.v1.RoundClosed)
            15 -> event = LessonEvent.Event.AttendanceOpened(_fieldValue as org.aula.app.rpc.lessons.v1.RoundOpened)
            16 -> event = LessonEvent.Event.AttendanceProgress(_fieldValue as org.aula.app.rpc.lessons.v1.RoundChanged)
            17 -> event = LessonEvent.Event.AttendanceClosed(_fieldValue as org.aula.app.rpc.lessons.v1.RoundClosed)
        }
    }

    return LessonEvent(event, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSetPulseRequest")
public fun SetPulseRequest?.orDefault(): org.aula.app.rpc.lessons.v1.SetPulseRequest = this ?: SetPulseRequest.defaultInstance

private fun SetPulseRequest.protoMergeImpl(plus: pbandk.Message?): SetPulseRequest = (plus as? SetPulseRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun SetPulseRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): SetPulseRequest {
    var lessonId = ""
    var state: org.aula.app.rpc.lessons.v1.PulseState = org.aula.app.rpc.lessons.v1.PulseState.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
            2 -> state = _fieldValue as org.aula.app.rpc.lessons.v1.PulseState
        }
    }

    return SetPulseRequest(lessonId, state, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSetPulseResponse")
public fun SetPulseResponse?.orDefault(): org.aula.app.rpc.lessons.v1.SetPulseResponse = this ?: SetPulseResponse.defaultInstance

private fun SetPulseResponse.protoMergeImpl(plus: pbandk.Message?): SetPulseResponse = (plus as? SetPulseResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun SetPulseResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): SetPulseResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return SetPulseResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForPulsePoint")
public fun PulsePoint?.orDefault(): org.aula.app.rpc.lessons.v1.PulsePoint = this ?: PulsePoint.defaultInstance

private fun PulsePoint.protoMergeImpl(plus: pbandk.Message?): PulsePoint = (plus as? PulsePoint)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun PulsePoint.Companion.decodeWithImpl(u: pbandk.MessageDecoder): PulsePoint {
    var minute = 0
    var okPercent = 0
    var lostCount = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> minute = _fieldValue as Int
            2 -> okPercent = _fieldValue as Int
            3 -> lostCount = _fieldValue as Int
        }
    }

    return PulsePoint(minute, okPercent, lostCount, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForMarker")
public fun Marker?.orDefault(): org.aula.app.rpc.lessons.v1.Marker = this ?: Marker.defaultInstance

private fun Marker.protoMergeImpl(plus: pbandk.Message?): Marker = (plus as? Marker)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Marker.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Marker {
    var minute = 0
    var kind: org.aula.app.rpc.lessons.v1.MarkerKind = org.aula.app.rpc.lessons.v1.MarkerKind.fromValue(0)
    var refId = ""
    var label = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> minute = _fieldValue as Int
            2 -> kind = _fieldValue as org.aula.app.rpc.lessons.v1.MarkerKind
            3 -> refId = _fieldValue as String
            4 -> label = _fieldValue as String
        }
    }

    return Marker(minute, kind, refId, label, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForPeakMoment")
public fun PeakMoment?.orDefault(): org.aula.app.rpc.lessons.v1.PeakMoment = this ?: PeakMoment.defaultInstance

private fun PeakMoment.protoMergeImpl(plus: pbandk.Message?): PeakMoment = (plus as? PeakMoment)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun PeakMoment.Companion.decodeWithImpl(u: pbandk.MessageDecoder): PeakMoment {
    var minute = 0
    var lostCount = 0
    var topDoubt = ""
    var slideNumber = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> minute = _fieldValue as Int
            2 -> lostCount = _fieldValue as Int
            3 -> topDoubt = _fieldValue as String
            4 -> slideNumber = _fieldValue as Int
        }
    }

    return PeakMoment(minute, lostCount, topDoubt, slideNumber, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForAttendanceTotals")
public fun AttendanceTotals?.orDefault(): org.aula.app.rpc.lessons.v1.AttendanceTotals = this ?: AttendanceTotals.defaultInstance

private fun AttendanceTotals.protoMergeImpl(plus: pbandk.Message?): AttendanceTotals = (plus as? AttendanceTotals)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun AttendanceTotals.Companion.decodeWithImpl(u: pbandk.MessageDecoder): AttendanceTotals {
    var present = 0
    var needsReview = 0
    var absent = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> present = _fieldValue as Int
            2 -> needsReview = _fieldValue as Int
            3 -> absent = _fieldValue as Int
        }
    }

    return AttendanceTotals(present, needsReview, absent, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetLessonReviewRequest")
public fun GetLessonReviewRequest?.orDefault(): org.aula.app.rpc.lessons.v1.GetLessonReviewRequest = this ?: GetLessonReviewRequest.defaultInstance

private fun GetLessonReviewRequest.protoMergeImpl(plus: pbandk.Message?): GetLessonReviewRequest = (plus as? GetLessonReviewRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetLessonReviewRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetLessonReviewRequest {
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
        }
    }

    return GetLessonReviewRequest(lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetLessonReviewResponse")
public fun GetLessonReviewResponse?.orDefault(): org.aula.app.rpc.lessons.v1.GetLessonReviewResponse = this ?: GetLessonReviewResponse.defaultInstance

private fun GetLessonReviewResponse.protoMergeImpl(plus: pbandk.Message?): GetLessonReviewResponse = (plus as? GetLessonReviewResponse)?.let {
    it.copy(
        pulseSeries = pulseSeries + plus.pulseSeries,
        markers = markers + plus.markers,
        peak = peak?.plus(plus.peak) ?: plus.peak,
        attendance = attendance?.plus(plus.attendance) ?: plus.attendance,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetLessonReviewResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetLessonReviewResponse {
    var lessonId = ""
    var courseId = ""
    var courseName = ""
    var number = 0
    var durationSeconds = 0
    var enrolledCount = 0
    var pulseSeries: pbandk.ListWithSize.Builder<org.aula.app.rpc.lessons.v1.PulsePoint>? = null
    var markers: pbandk.ListWithSize.Builder<org.aula.app.rpc.lessons.v1.Marker>? = null
    var peak: org.aula.app.rpc.lessons.v1.PeakMoment? = null
    var attendance: org.aula.app.rpc.lessons.v1.AttendanceTotals? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
            2 -> courseId = _fieldValue as String
            3 -> courseName = _fieldValue as String
            4 -> number = _fieldValue as Int
            5 -> durationSeconds = _fieldValue as Int
            6 -> enrolledCount = _fieldValue as Int
            7 -> pulseSeries = (pulseSeries ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.lessons.v1.PulsePoint> }
            8 -> markers = (markers ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.lessons.v1.Marker> }
            9 -> peak = _fieldValue as org.aula.app.rpc.lessons.v1.PeakMoment
            10 -> attendance = _fieldValue as org.aula.app.rpc.lessons.v1.AttendanceTotals
        }
    }

    return GetLessonReviewResponse(lessonId, courseId, courseName, number,
        durationSeconds, enrolledCount, pbandk.ListWithSize.Builder.fixed(pulseSeries), pbandk.ListWithSize.Builder.fixed(markers),
        peak, attendance, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForMaterial")
public fun Material?.orDefault(): org.aula.app.rpc.lessons.v1.Material = this ?: Material.defaultInstance

private fun Material.protoMergeImpl(plus: pbandk.Message?): Material = (plus as? Material)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Material.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Material {
    var id = ""
    var lessonId = ""
    var filename = ""
    var ready = false

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> lessonId = _fieldValue as String
            3 -> filename = _fieldValue as String
            4 -> ready = _fieldValue as Boolean
        }
    }

    return Material(id, lessonId, filename, ready, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForUploadMaterialRequest")
public fun UploadMaterialRequest?.orDefault(): org.aula.app.rpc.lessons.v1.UploadMaterialRequest = this ?: UploadMaterialRequest.defaultInstance

private fun UploadMaterialRequest.protoMergeImpl(plus: pbandk.Message?): UploadMaterialRequest = (plus as? UploadMaterialRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun UploadMaterialRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): UploadMaterialRequest {
    var lessonId = ""
    var filename = ""
    var content: pbandk.ByteArr = pbandk.ByteArr.empty

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
            2 -> filename = _fieldValue as String
            3 -> content = _fieldValue as pbandk.ByteArr
        }
    }

    return UploadMaterialRequest(lessonId, filename, content, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForUploadMaterialResponse")
public fun UploadMaterialResponse?.orDefault(): org.aula.app.rpc.lessons.v1.UploadMaterialResponse = this ?: UploadMaterialResponse.defaultInstance

private fun UploadMaterialResponse.protoMergeImpl(plus: pbandk.Message?): UploadMaterialResponse = (plus as? UploadMaterialResponse)?.let {
    it.copy(
        material = material?.plus(plus.material) ?: plus.material,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun UploadMaterialResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): UploadMaterialResponse {
    var material: org.aula.app.rpc.lessons.v1.Material? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> material = _fieldValue as org.aula.app.rpc.lessons.v1.Material
        }
    }

    return UploadMaterialResponse(material, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListMaterialsRequest")
public fun ListMaterialsRequest?.orDefault(): org.aula.app.rpc.lessons.v1.ListMaterialsRequest = this ?: ListMaterialsRequest.defaultInstance

private fun ListMaterialsRequest.protoMergeImpl(plus: pbandk.Message?): ListMaterialsRequest = (plus as? ListMaterialsRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListMaterialsRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListMaterialsRequest {
    var lessonId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
        }
    }

    return ListMaterialsRequest(lessonId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListMaterialsResponse")
public fun ListMaterialsResponse?.orDefault(): org.aula.app.rpc.lessons.v1.ListMaterialsResponse = this ?: ListMaterialsResponse.defaultInstance

private fun ListMaterialsResponse.protoMergeImpl(plus: pbandk.Message?): ListMaterialsResponse = (plus as? ListMaterialsResponse)?.let {
    it.copy(
        materials = materials + plus.materials,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListMaterialsResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListMaterialsResponse {
    var materials: pbandk.ListWithSize.Builder<org.aula.app.rpc.lessons.v1.Material>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> materials = (materials ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.lessons.v1.Material> }
        }
    }

    return ListMaterialsResponse(pbandk.ListWithSize.Builder.fixed(materials), unknownFields)
}
