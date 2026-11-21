@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.courses.v1

@pbandk.Export
public sealed class LessonStatus(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.courses.v1.LessonStatus && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "LessonStatus.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : LessonStatus(0, "LESSON_STATUS_UNSPECIFIED")
    public object LOBBY : LessonStatus(1, "LESSON_STATUS_LOBBY")
    public object LIVE : LessonStatus(2, "LESSON_STATUS_LIVE")
    public object ENDED : LessonStatus(3, "LESSON_STATUS_ENDED")
    public object CANCELLED : LessonStatus(4, "LESSON_STATUS_CANCELLED")
    public class UNRECOGNIZED(value: Int) : LessonStatus(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.courses.v1.LessonStatus> {
        public val values: List<org.aula.app.rpc.courses.v1.LessonStatus> by lazy { listOf(UNSPECIFIED, LOBBY, LIVE, ENDED, CANCELLED) }
        override fun fromValue(value: Int): org.aula.app.rpc.courses.v1.LessonStatus = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.courses.v1.LessonStatus = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No LessonStatus with name: $name")
    }
}

@pbandk.Export
public data class LiveLesson(
    val id: String = "",
    val number: Int = 0,
    val status: org.aula.app.rpc.courses.v1.LessonStatus = org.aula.app.rpc.courses.v1.LessonStatus.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.LiveLesson = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.LiveLesson> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.LiveLesson> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.LiveLesson by lazy { org.aula.app.rpc.courses.v1.LiveLesson() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.LiveLesson = org.aula.app.rpc.courses.v1.LiveLesson.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.LiveLesson> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.LiveLesson",
            messageClass = org.aula.app.rpc.courses.v1.LiveLesson::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.courses.v1.LiveLesson::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "number",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "number",
                        value = org.aula.app.rpc.courses.v1.LiveLesson::number
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.courses.v1.LessonStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.courses.v1.LiveLesson::status
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CourseSummary(
    val id: String = "",
    val name: String = "",
    val code: String = "",
    val studentCount: Int = 0,
    val liveLesson: org.aula.app.rpc.courses.v1.LiveLesson? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.CourseSummary = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.CourseSummary> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.CourseSummary> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.CourseSummary by lazy { org.aula.app.rpc.courses.v1.CourseSummary() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.CourseSummary = org.aula.app.rpc.courses.v1.CourseSummary.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.CourseSummary> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.CourseSummary",
            messageClass = org.aula.app.rpc.courses.v1.CourseSummary::class,
            messageCompanion = this,
            fields = buildList(5) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.courses.v1.CourseSummary::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.courses.v1.CourseSummary::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "code",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "code",
                        value = org.aula.app.rpc.courses.v1.CourseSummary::code
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_count",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "studentCount",
                        value = org.aula.app.rpc.courses.v1.CourseSummary::studentCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "live_lesson",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.courses.v1.LiveLesson.Companion),
                        jsonName = "liveLesson",
                        value = org.aula.app.rpc.courses.v1.CourseSummary::liveLesson
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class PastLesson(
    val id: String = "",
    val number: Int = 0,
    val date: pbandk.wkt.Timestamp? = null,
    val attendeeCount: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.PastLesson = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.PastLesson> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.PastLesson> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.PastLesson by lazy { org.aula.app.rpc.courses.v1.PastLesson() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.PastLesson = org.aula.app.rpc.courses.v1.PastLesson.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.PastLesson> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.PastLesson",
            messageClass = org.aula.app.rpc.courses.v1.PastLesson::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.courses.v1.PastLesson::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "number",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "number",
                        value = org.aula.app.rpc.courses.v1.PastLesson::number
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "date",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "date",
                        value = org.aula.app.rpc.courses.v1.PastLesson::date
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "attendee_count",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "attendeeCount",
                        value = org.aula.app.rpc.courses.v1.PastLesson::attendeeCount
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CourseDetail(
    val id: String = "",
    val name: String = "",
    val code: String = "",
    val joinCode: String = "",
    val studentCount: Int = 0,
    val liveLesson: org.aula.app.rpc.courses.v1.LiveLesson? = null,
    val lessons: List<org.aula.app.rpc.courses.v1.PastLesson> = emptyList(),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.CourseDetail = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.CourseDetail> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.CourseDetail> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.CourseDetail by lazy { org.aula.app.rpc.courses.v1.CourseDetail() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.CourseDetail = org.aula.app.rpc.courses.v1.CourseDetail.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.CourseDetail> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.CourseDetail",
            messageClass = org.aula.app.rpc.courses.v1.CourseDetail::class,
            messageCompanion = this,
            fields = buildList(7) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.courses.v1.CourseDetail::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.courses.v1.CourseDetail::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "code",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "code",
                        value = org.aula.app.rpc.courses.v1.CourseDetail::code
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "join_code",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "joinCode",
                        value = org.aula.app.rpc.courses.v1.CourseDetail::joinCode
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_count",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "studentCount",
                        value = org.aula.app.rpc.courses.v1.CourseDetail::studentCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "live_lesson",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.courses.v1.LiveLesson.Companion),
                        jsonName = "liveLesson",
                        value = org.aula.app.rpc.courses.v1.CourseDetail::liveLesson
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lessons",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.courses.v1.PastLesson>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.courses.v1.PastLesson.Companion)),
                        jsonName = "lessons",
                        value = org.aula.app.rpc.courses.v1.CourseDetail::lessons
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ListCoursesRequest(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.ListCoursesRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.ListCoursesRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.ListCoursesRequest> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.ListCoursesRequest by lazy { org.aula.app.rpc.courses.v1.ListCoursesRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.ListCoursesRequest = org.aula.app.rpc.courses.v1.ListCoursesRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.ListCoursesRequest> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.ListCoursesRequest",
            messageClass = org.aula.app.rpc.courses.v1.ListCoursesRequest::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class ListCoursesResponse(
    val courses: List<org.aula.app.rpc.courses.v1.CourseSummary> = emptyList(),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.ListCoursesResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.ListCoursesResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.ListCoursesResponse> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.ListCoursesResponse by lazy { org.aula.app.rpc.courses.v1.ListCoursesResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.ListCoursesResponse = org.aula.app.rpc.courses.v1.ListCoursesResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.ListCoursesResponse> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.ListCoursesResponse",
            messageClass = org.aula.app.rpc.courses.v1.ListCoursesResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "courses",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.courses.v1.CourseSummary>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.courses.v1.CourseSummary.Companion)),
                        jsonName = "courses",
                        value = org.aula.app.rpc.courses.v1.ListCoursesResponse::courses
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CreateCourseRequest(
    val name: String = "",
    val code: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.CreateCourseRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.CreateCourseRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.CreateCourseRequest> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.CreateCourseRequest by lazy { org.aula.app.rpc.courses.v1.CreateCourseRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.CreateCourseRequest = org.aula.app.rpc.courses.v1.CreateCourseRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.CreateCourseRequest> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.CreateCourseRequest",
            messageClass = org.aula.app.rpc.courses.v1.CreateCourseRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(6, 114, 4, 16, 1, 24, 100))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.courses.v1.CreateCourseRequest::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "code",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "code",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(4, 114, 2, 24, 20))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.courses.v1.CreateCourseRequest::code
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CreateCourseResponse(
    val course: org.aula.app.rpc.courses.v1.CourseDetail? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.CreateCourseResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.CreateCourseResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.CreateCourseResponse> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.CreateCourseResponse by lazy { org.aula.app.rpc.courses.v1.CreateCourseResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.CreateCourseResponse = org.aula.app.rpc.courses.v1.CreateCourseResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.CreateCourseResponse> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.CreateCourseResponse",
            messageClass = org.aula.app.rpc.courses.v1.CreateCourseResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.courses.v1.CourseDetail.Companion),
                        jsonName = "course",
                        value = org.aula.app.rpc.courses.v1.CreateCourseResponse::course
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetCourseRequest(
    val courseId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.GetCourseRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.GetCourseRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.GetCourseRequest> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.GetCourseRequest by lazy { org.aula.app.rpc.courses.v1.GetCourseRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.GetCourseRequest = org.aula.app.rpc.courses.v1.GetCourseRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.GetCourseRequest> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.GetCourseRequest",
            messageClass = org.aula.app.rpc.courses.v1.GetCourseRequest::class,
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
                        value = org.aula.app.rpc.courses.v1.GetCourseRequest::courseId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetCourseResponse(
    val course: org.aula.app.rpc.courses.v1.CourseDetail? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.GetCourseResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.GetCourseResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.GetCourseResponse> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.GetCourseResponse by lazy { org.aula.app.rpc.courses.v1.GetCourseResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.GetCourseResponse = org.aula.app.rpc.courses.v1.GetCourseResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.GetCourseResponse> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.GetCourseResponse",
            messageClass = org.aula.app.rpc.courses.v1.GetCourseResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.courses.v1.CourseDetail.Companion),
                        jsonName = "course",
                        value = org.aula.app.rpc.courses.v1.GetCourseResponse::course
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class JoinCourseRequest(
    val courseId: String = "",
    val joinCode: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.JoinCourseRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.JoinCourseRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.JoinCourseRequest> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.JoinCourseRequest by lazy { org.aula.app.rpc.courses.v1.JoinCourseRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.JoinCourseRequest = org.aula.app.rpc.courses.v1.JoinCourseRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.JoinCourseRequest> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.JoinCourseRequest",
            messageClass = org.aula.app.rpc.courses.v1.JoinCourseRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
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
                        value = org.aula.app.rpc.courses.v1.JoinCourseRequest::courseId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "join_code",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "joinCode",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(6, 114, 4, 16, 3, 24, 20))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.courses.v1.JoinCourseRequest::joinCode
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class JoinCourseResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.JoinCourseResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.JoinCourseResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.JoinCourseResponse> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.JoinCourseResponse by lazy { org.aula.app.rpc.courses.v1.JoinCourseResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.JoinCourseResponse = org.aula.app.rpc.courses.v1.JoinCourseResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.JoinCourseResponse> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.JoinCourseResponse",
            messageClass = org.aula.app.rpc.courses.v1.JoinCourseResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class StudentSummary(
    val id: String = "",
    val name: String = "",
    val attendanceRate: Double = 0.0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.StudentSummary = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.StudentSummary> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.StudentSummary> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.StudentSummary by lazy { org.aula.app.rpc.courses.v1.StudentSummary() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.StudentSummary = org.aula.app.rpc.courses.v1.StudentSummary.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.StudentSummary> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.StudentSummary",
            messageClass = org.aula.app.rpc.courses.v1.StudentSummary::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.courses.v1.StudentSummary::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.courses.v1.StudentSummary::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "attendance_rate",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Double(),
                        jsonName = "attendanceRate",
                        value = org.aula.app.rpc.courses.v1.StudentSummary::attendanceRate
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ListCourseStudentsRequest(
    val courseId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.ListCourseStudentsRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.ListCourseStudentsRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.ListCourseStudentsRequest> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.ListCourseStudentsRequest by lazy { org.aula.app.rpc.courses.v1.ListCourseStudentsRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.ListCourseStudentsRequest = org.aula.app.rpc.courses.v1.ListCourseStudentsRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.ListCourseStudentsRequest> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.ListCourseStudentsRequest",
            messageClass = org.aula.app.rpc.courses.v1.ListCourseStudentsRequest::class,
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
                        value = org.aula.app.rpc.courses.v1.ListCourseStudentsRequest::courseId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ListCourseStudentsResponse(
    val students: List<org.aula.app.rpc.courses.v1.StudentSummary> = emptyList(),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.courses.v1.ListCourseStudentsResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.ListCourseStudentsResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.courses.v1.ListCourseStudentsResponse> {
        public val defaultInstance: org.aula.app.rpc.courses.v1.ListCourseStudentsResponse by lazy { org.aula.app.rpc.courses.v1.ListCourseStudentsResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.courses.v1.ListCourseStudentsResponse = org.aula.app.rpc.courses.v1.ListCourseStudentsResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.courses.v1.ListCourseStudentsResponse> = pbandk.MessageDescriptor(
            fullName = "aula.courses.v1.ListCourseStudentsResponse",
            messageClass = org.aula.app.rpc.courses.v1.ListCourseStudentsResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "students",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.courses.v1.StudentSummary>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.courses.v1.StudentSummary.Companion)),
                        jsonName = "students",
                        value = org.aula.app.rpc.courses.v1.ListCourseStudentsResponse::students
                    )
                )
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForLiveLesson")
public fun LiveLesson?.orDefault(): org.aula.app.rpc.courses.v1.LiveLesson = this ?: LiveLesson.defaultInstance

private fun LiveLesson.protoMergeImpl(plus: pbandk.Message?): LiveLesson = (plus as? LiveLesson)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LiveLesson.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LiveLesson {
    var id = ""
    var number = 0
    var status: org.aula.app.rpc.courses.v1.LessonStatus = org.aula.app.rpc.courses.v1.LessonStatus.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> number = _fieldValue as Int
            3 -> status = _fieldValue as org.aula.app.rpc.courses.v1.LessonStatus
        }
    }

    return LiveLesson(id, number, status, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCourseSummary")
public fun CourseSummary?.orDefault(): org.aula.app.rpc.courses.v1.CourseSummary = this ?: CourseSummary.defaultInstance

private fun CourseSummary.protoMergeImpl(plus: pbandk.Message?): CourseSummary = (plus as? CourseSummary)?.let {
    it.copy(
        liveLesson = liveLesson?.plus(plus.liveLesson) ?: plus.liveLesson,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CourseSummary.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CourseSummary {
    var id = ""
    var name = ""
    var code = ""
    var studentCount = 0
    var liveLesson: org.aula.app.rpc.courses.v1.LiveLesson? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> code = _fieldValue as String
            4 -> studentCount = _fieldValue as Int
            5 -> liveLesson = _fieldValue as org.aula.app.rpc.courses.v1.LiveLesson
        }
    }

    return CourseSummary(id, name, code, studentCount,
        liveLesson, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForPastLesson")
public fun PastLesson?.orDefault(): org.aula.app.rpc.courses.v1.PastLesson = this ?: PastLesson.defaultInstance

private fun PastLesson.protoMergeImpl(plus: pbandk.Message?): PastLesson = (plus as? PastLesson)?.let {
    it.copy(
        date = date?.plus(plus.date) ?: plus.date,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun PastLesson.Companion.decodeWithImpl(u: pbandk.MessageDecoder): PastLesson {
    var id = ""
    var number = 0
    var date: pbandk.wkt.Timestamp? = null
    var attendeeCount = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> number = _fieldValue as Int
            3 -> date = _fieldValue as pbandk.wkt.Timestamp
            4 -> attendeeCount = _fieldValue as Int
        }
    }

    return PastLesson(id, number, date, attendeeCount, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCourseDetail")
public fun CourseDetail?.orDefault(): org.aula.app.rpc.courses.v1.CourseDetail = this ?: CourseDetail.defaultInstance

private fun CourseDetail.protoMergeImpl(plus: pbandk.Message?): CourseDetail = (plus as? CourseDetail)?.let {
    it.copy(
        liveLesson = liveLesson?.plus(plus.liveLesson) ?: plus.liveLesson,
        lessons = lessons + plus.lessons,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CourseDetail.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CourseDetail {
    var id = ""
    var name = ""
    var code = ""
    var joinCode = ""
    var studentCount = 0
    var liveLesson: org.aula.app.rpc.courses.v1.LiveLesson? = null
    var lessons: pbandk.ListWithSize.Builder<org.aula.app.rpc.courses.v1.PastLesson>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> code = _fieldValue as String
            4 -> joinCode = _fieldValue as String
            5 -> studentCount = _fieldValue as Int
            6 -> liveLesson = _fieldValue as org.aula.app.rpc.courses.v1.LiveLesson
            7 -> lessons = (lessons ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.courses.v1.PastLesson> }
        }
    }

    return CourseDetail(id, name, code, joinCode,
        studentCount, liveLesson, pbandk.ListWithSize.Builder.fixed(lessons), unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListCoursesRequest")
public fun ListCoursesRequest?.orDefault(): org.aula.app.rpc.courses.v1.ListCoursesRequest = this ?: ListCoursesRequest.defaultInstance

private fun ListCoursesRequest.protoMergeImpl(plus: pbandk.Message?): ListCoursesRequest = (plus as? ListCoursesRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListCoursesRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListCoursesRequest {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return ListCoursesRequest(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListCoursesResponse")
public fun ListCoursesResponse?.orDefault(): org.aula.app.rpc.courses.v1.ListCoursesResponse = this ?: ListCoursesResponse.defaultInstance

private fun ListCoursesResponse.protoMergeImpl(plus: pbandk.Message?): ListCoursesResponse = (plus as? ListCoursesResponse)?.let {
    it.copy(
        courses = courses + plus.courses,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListCoursesResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListCoursesResponse {
    var courses: pbandk.ListWithSize.Builder<org.aula.app.rpc.courses.v1.CourseSummary>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> courses = (courses ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.courses.v1.CourseSummary> }
        }
    }

    return ListCoursesResponse(pbandk.ListWithSize.Builder.fixed(courses), unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCreateCourseRequest")
public fun CreateCourseRequest?.orDefault(): org.aula.app.rpc.courses.v1.CreateCourseRequest = this ?: CreateCourseRequest.defaultInstance

private fun CreateCourseRequest.protoMergeImpl(plus: pbandk.Message?): CreateCourseRequest = (plus as? CreateCourseRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CreateCourseRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CreateCourseRequest {
    var name = ""
    var code = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> name = _fieldValue as String
            2 -> code = _fieldValue as String
        }
    }

    return CreateCourseRequest(name, code, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCreateCourseResponse")
public fun CreateCourseResponse?.orDefault(): org.aula.app.rpc.courses.v1.CreateCourseResponse = this ?: CreateCourseResponse.defaultInstance

private fun CreateCourseResponse.protoMergeImpl(plus: pbandk.Message?): CreateCourseResponse = (plus as? CreateCourseResponse)?.let {
    it.copy(
        course = course?.plus(plus.course) ?: plus.course,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CreateCourseResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CreateCourseResponse {
    var course: org.aula.app.rpc.courses.v1.CourseDetail? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> course = _fieldValue as org.aula.app.rpc.courses.v1.CourseDetail
        }
    }

    return CreateCourseResponse(course, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetCourseRequest")
public fun GetCourseRequest?.orDefault(): org.aula.app.rpc.courses.v1.GetCourseRequest = this ?: GetCourseRequest.defaultInstance

private fun GetCourseRequest.protoMergeImpl(plus: pbandk.Message?): GetCourseRequest = (plus as? GetCourseRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetCourseRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetCourseRequest {
    var courseId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> courseId = _fieldValue as String
        }
    }

    return GetCourseRequest(courseId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetCourseResponse")
public fun GetCourseResponse?.orDefault(): org.aula.app.rpc.courses.v1.GetCourseResponse = this ?: GetCourseResponse.defaultInstance

private fun GetCourseResponse.protoMergeImpl(plus: pbandk.Message?): GetCourseResponse = (plus as? GetCourseResponse)?.let {
    it.copy(
        course = course?.plus(plus.course) ?: plus.course,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetCourseResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetCourseResponse {
    var course: org.aula.app.rpc.courses.v1.CourseDetail? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> course = _fieldValue as org.aula.app.rpc.courses.v1.CourseDetail
        }
    }

    return GetCourseResponse(course, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForJoinCourseRequest")
public fun JoinCourseRequest?.orDefault(): org.aula.app.rpc.courses.v1.JoinCourseRequest = this ?: JoinCourseRequest.defaultInstance

private fun JoinCourseRequest.protoMergeImpl(plus: pbandk.Message?): JoinCourseRequest = (plus as? JoinCourseRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun JoinCourseRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): JoinCourseRequest {
    var courseId = ""
    var joinCode = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> courseId = _fieldValue as String
            2 -> joinCode = _fieldValue as String
        }
    }

    return JoinCourseRequest(courseId, joinCode, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForJoinCourseResponse")
public fun JoinCourseResponse?.orDefault(): org.aula.app.rpc.courses.v1.JoinCourseResponse = this ?: JoinCourseResponse.defaultInstance

private fun JoinCourseResponse.protoMergeImpl(plus: pbandk.Message?): JoinCourseResponse = (plus as? JoinCourseResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun JoinCourseResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): JoinCourseResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return JoinCourseResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForStudentSummary")
public fun StudentSummary?.orDefault(): org.aula.app.rpc.courses.v1.StudentSummary = this ?: StudentSummary.defaultInstance

private fun StudentSummary.protoMergeImpl(plus: pbandk.Message?): StudentSummary = (plus as? StudentSummary)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun StudentSummary.Companion.decodeWithImpl(u: pbandk.MessageDecoder): StudentSummary {
    var id = ""
    var name = ""
    var attendanceRate = 0.0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> attendanceRate = _fieldValue as Double
        }
    }

    return StudentSummary(id, name, attendanceRate, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListCourseStudentsRequest")
public fun ListCourseStudentsRequest?.orDefault(): org.aula.app.rpc.courses.v1.ListCourseStudentsRequest = this ?: ListCourseStudentsRequest.defaultInstance

private fun ListCourseStudentsRequest.protoMergeImpl(plus: pbandk.Message?): ListCourseStudentsRequest = (plus as? ListCourseStudentsRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListCourseStudentsRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListCourseStudentsRequest {
    var courseId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> courseId = _fieldValue as String
        }
    }

    return ListCourseStudentsRequest(courseId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListCourseStudentsResponse")
public fun ListCourseStudentsResponse?.orDefault(): org.aula.app.rpc.courses.v1.ListCourseStudentsResponse = this ?: ListCourseStudentsResponse.defaultInstance

private fun ListCourseStudentsResponse.protoMergeImpl(plus: pbandk.Message?): ListCourseStudentsResponse = (plus as? ListCourseStudentsResponse)?.let {
    it.copy(
        students = students + plus.students,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListCourseStudentsResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListCourseStudentsResponse {
    var students: pbandk.ListWithSize.Builder<org.aula.app.rpc.courses.v1.StudentSummary>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> students = (students ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.courses.v1.StudentSummary> }
        }
    }

    return ListCourseStudentsResponse(pbandk.ListWithSize.Builder.fixed(students), unknownFields)
}
