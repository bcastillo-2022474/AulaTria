@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.quizzes.v1

@pbandk.Export
public sealed class QuizStatus(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.quizzes.v1.QuizStatus && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "QuizStatus.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : QuizStatus(0, "QUIZ_STATUS_UNSPECIFIED")
    public object DRAFT : QuizStatus(1, "QUIZ_STATUS_DRAFT")
    public object LIVE : QuizStatus(2, "QUIZ_STATUS_LIVE")
    public object CLOSED : QuizStatus(3, "QUIZ_STATUS_CLOSED")
    public class UNRECOGNIZED(value: Int) : QuizStatus(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.quizzes.v1.QuizStatus> {
        public val values: List<org.aula.app.rpc.quizzes.v1.QuizStatus> by lazy { listOf(UNSPECIFIED, DRAFT, LIVE, CLOSED) }
        override fun fromValue(value: Int): org.aula.app.rpc.quizzes.v1.QuizStatus = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.quizzes.v1.QuizStatus = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No QuizStatus with name: $name")
    }
}

@pbandk.Export
public data class QuizOption(
    val label: String = "",
    val text: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.QuizOption = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.QuizOption> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.QuizOption> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.QuizOption by lazy { org.aula.app.rpc.quizzes.v1.QuizOption() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.QuizOption = org.aula.app.rpc.quizzes.v1.QuizOption.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.QuizOption> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.QuizOption",
            messageClass = org.aula.app.rpc.quizzes.v1.QuizOption::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "label",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "label",
                        value = org.aula.app.rpc.quizzes.v1.QuizOption::label
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "text",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "text",
                        value = org.aula.app.rpc.quizzes.v1.QuizOption::text
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class Quiz(
    val id: String = "",
    val lessonId: String = "",
    val status: org.aula.app.rpc.quizzes.v1.QuizStatus = org.aula.app.rpc.quizzes.v1.QuizStatus.fromValue(0),
    val question: String = "",
    val options: List<org.aula.app.rpc.quizzes.v1.QuizOption> = emptyList(),
    val rationale: String = "",
    val slideNumber: Int = 0,
    val timeLimitSeconds: Int = 0,
    val closesAt: pbandk.wkt.Timestamp? = null,
    val answeredCount: Int = 0,
    val enrolledCount: Int = 0,
    val correctLabel: String = "",
    val countsByLabel: Map<String, Int> = emptyMap(),
    val correctPercent: Int = 0,
    val insight: String = "",
    val myLabel: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.Quiz = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.Quiz> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.Quiz> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.Quiz by lazy { org.aula.app.rpc.quizzes.v1.Quiz() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.Quiz = org.aula.app.rpc.quizzes.v1.Quiz.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.Quiz> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.Quiz",
            messageClass = org.aula.app.rpc.quizzes.v1.Quiz::class,
            messageCompanion = this,
            fields = buildList(16) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.quizzes.v1.QuizStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::status
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "question",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "question",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::question
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "options",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.quizzes.v1.QuizOption>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.quizzes.v1.QuizOption.Companion)),
                        jsonName = "options",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::options
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "rationale",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "rationale",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::rationale
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "slide_number",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "slideNumber",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::slideNumber
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "time_limit_seconds",
                        number = 8,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "timeLimitSeconds",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::timeLimitSeconds
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "closes_at",
                        number = 9,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "closesAt",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::closesAt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "answered_count",
                        number = 10,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "answeredCount",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::answeredCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "enrolled_count",
                        number = 11,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "enrolledCount",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::enrolledCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "correct_label",
                        number = 12,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "correctLabel",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::correctLabel
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "counts_by_label",
                        number = 13,
                        type = pbandk.FieldDescriptor.Type.Map<String, Int>(keyType = pbandk.FieldDescriptor.Type.Primitive.String(), valueType = pbandk.FieldDescriptor.Type.Primitive.Int32()),
                        jsonName = "countsByLabel",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::countsByLabel
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "correct_percent",
                        number = 14,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "correctPercent",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::correctPercent
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "insight",
                        number = 15,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "insight",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::insight
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "my_label",
                        number = 16,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "myLabel",
                        value = org.aula.app.rpc.quizzes.v1.Quiz::myLabel
                    )
                )
            }
        )
    }

    public data class CountsByLabelEntry(
        override val key: String = "",
        override val value: Int = 0,
        override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
    ) : pbandk.Message, Map.Entry<String, Int> {
        override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry = protoMergeImpl(other)
        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry> get() = Companion.descriptor
        override val protoSize: Int by lazy { super.protoSize }
        public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry> {
            public val defaultInstance: org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry by lazy { org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry() }
            override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry = org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry.decodeWithImpl(u)

            override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry> = pbandk.MessageDescriptor(
                fullName = "aula.quizzes.v1.Quiz.CountsByLabelEntry",
                messageClass = org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry::class,
                messageCompanion = this,
                fields = buildList(2) {
                    add(
                        pbandk.FieldDescriptor(
                            messageDescriptor = this@Companion::descriptor,
                            name = "key",
                            number = 1,
                            type = pbandk.FieldDescriptor.Type.Primitive.String(),
                            jsonName = "key",
                            value = org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry::key
                        )
                    )
                    add(
                        pbandk.FieldDescriptor(
                            messageDescriptor = this@Companion::descriptor,
                            name = "value",
                            number = 2,
                            type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                            jsonName = "value",
                            value = org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry::value
                        )
                    )
                }
            )
        }
    }
}

@pbandk.Export
public data class DraftQuizRequest(
    val lessonId: String = "",
    val slideNumber: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.DraftQuizRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.DraftQuizRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.DraftQuizRequest> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.DraftQuizRequest by lazy { org.aula.app.rpc.quizzes.v1.DraftQuizRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.DraftQuizRequest = org.aula.app.rpc.quizzes.v1.DraftQuizRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.DraftQuizRequest> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.DraftQuizRequest",
            messageClass = org.aula.app.rpc.quizzes.v1.DraftQuizRequest::class,
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
                        value = org.aula.app.rpc.quizzes.v1.DraftQuizRequest::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "slide_number",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "slideNumber",
                        value = org.aula.app.rpc.quizzes.v1.DraftQuizRequest::slideNumber
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class DraftQuizResponse(
    val quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.DraftQuizResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.DraftQuizResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.DraftQuizResponse> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.DraftQuizResponse by lazy { org.aula.app.rpc.quizzes.v1.DraftQuizResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.DraftQuizResponse = org.aula.app.rpc.quizzes.v1.DraftQuizResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.DraftQuizResponse> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.DraftQuizResponse",
            messageClass = org.aula.app.rpc.quizzes.v1.DraftQuizResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.quizzes.v1.Quiz.Companion),
                        jsonName = "quiz",
                        value = org.aula.app.rpc.quizzes.v1.DraftQuizResponse::quiz
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RegenerateQuizRequest(
    val quizId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest by lazy { org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest = org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.RegenerateQuizRequest",
            messageClass = org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "quizId",
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
                        value = org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest::quizId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RegenerateQuizResponse(
    val quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse by lazy { org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse = org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.RegenerateQuizResponse",
            messageClass = org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.quizzes.v1.Quiz.Companion),
                        jsonName = "quiz",
                        value = org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse::quiz
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class UpdateQuizTimeRequest(
    val quizId: String = "",
    val timeLimitSeconds: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest by lazy { org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest = org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.UpdateQuizTimeRequest",
            messageClass = org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "quizId",
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
                        value = org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest::quizId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "time_limit_seconds",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "timeLimitSeconds",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 26, 5, 24, -84, 2, 40, 10))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest::timeLimitSeconds
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class UpdateQuizTimeResponse(
    val quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse by lazy { org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse = org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.UpdateQuizTimeResponse",
            messageClass = org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.quizzes.v1.Quiz.Companion),
                        jsonName = "quiz",
                        value = org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse::quiz
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LaunchQuizRequest(
    val quizId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.LaunchQuizRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.LaunchQuizRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.LaunchQuizRequest> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.LaunchQuizRequest by lazy { org.aula.app.rpc.quizzes.v1.LaunchQuizRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.LaunchQuizRequest = org.aula.app.rpc.quizzes.v1.LaunchQuizRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.LaunchQuizRequest> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.LaunchQuizRequest",
            messageClass = org.aula.app.rpc.quizzes.v1.LaunchQuizRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "quizId",
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
                        value = org.aula.app.rpc.quizzes.v1.LaunchQuizRequest::quizId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LaunchQuizResponse(
    val quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.LaunchQuizResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.LaunchQuizResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.LaunchQuizResponse> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.LaunchQuizResponse by lazy { org.aula.app.rpc.quizzes.v1.LaunchQuizResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.LaunchQuizResponse = org.aula.app.rpc.quizzes.v1.LaunchQuizResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.LaunchQuizResponse> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.LaunchQuizResponse",
            messageClass = org.aula.app.rpc.quizzes.v1.LaunchQuizResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.quizzes.v1.Quiz.Companion),
                        jsonName = "quiz",
                        value = org.aula.app.rpc.quizzes.v1.LaunchQuizResponse::quiz
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class AnswerQuizRequest(
    val quizId: String = "",
    val label: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.AnswerQuizRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.AnswerQuizRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.AnswerQuizRequest> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.AnswerQuizRequest by lazy { org.aula.app.rpc.quizzes.v1.AnswerQuizRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.AnswerQuizRequest = org.aula.app.rpc.quizzes.v1.AnswerQuizRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.AnswerQuizRequest> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.AnswerQuizRequest",
            messageClass = org.aula.app.rpc.quizzes.v1.AnswerQuizRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "quizId",
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
                        value = org.aula.app.rpc.quizzes.v1.AnswerQuizRequest::quizId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "label",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "label",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(11, 114, 9, 50, 7, 94, 91, 65, 45, 68, 93, 36))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.quizzes.v1.AnswerQuizRequest::label
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class AnswerQuizResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.AnswerQuizResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.AnswerQuizResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.AnswerQuizResponse> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.AnswerQuizResponse by lazy { org.aula.app.rpc.quizzes.v1.AnswerQuizResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.AnswerQuizResponse = org.aula.app.rpc.quizzes.v1.AnswerQuizResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.AnswerQuizResponse> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.AnswerQuizResponse",
            messageClass = org.aula.app.rpc.quizzes.v1.AnswerQuizResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class CloseQuizRequest(
    val quizId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.CloseQuizRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.CloseQuizRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.CloseQuizRequest> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.CloseQuizRequest by lazy { org.aula.app.rpc.quizzes.v1.CloseQuizRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.CloseQuizRequest = org.aula.app.rpc.quizzes.v1.CloseQuizRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.CloseQuizRequest> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.CloseQuizRequest",
            messageClass = org.aula.app.rpc.quizzes.v1.CloseQuizRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "quizId",
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
                        value = org.aula.app.rpc.quizzes.v1.CloseQuizRequest::quizId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseQuizResponse(
    val quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.CloseQuizResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.CloseQuizResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.CloseQuizResponse> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.CloseQuizResponse by lazy { org.aula.app.rpc.quizzes.v1.CloseQuizResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.CloseQuizResponse = org.aula.app.rpc.quizzes.v1.CloseQuizResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.CloseQuizResponse> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.CloseQuizResponse",
            messageClass = org.aula.app.rpc.quizzes.v1.CloseQuizResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.quizzes.v1.Quiz.Companion),
                        jsonName = "quiz",
                        value = org.aula.app.rpc.quizzes.v1.CloseQuizResponse::quiz
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetQuizRequest(
    val quizId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.GetQuizRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.GetQuizRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.GetQuizRequest> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.GetQuizRequest by lazy { org.aula.app.rpc.quizzes.v1.GetQuizRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.GetQuizRequest = org.aula.app.rpc.quizzes.v1.GetQuizRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.GetQuizRequest> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.GetQuizRequest",
            messageClass = org.aula.app.rpc.quizzes.v1.GetQuizRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "quizId",
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
                        value = org.aula.app.rpc.quizzes.v1.GetQuizRequest::quizId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetQuizResponse(
    val quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.quizzes.v1.GetQuizResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.GetQuizResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.quizzes.v1.GetQuizResponse> {
        public val defaultInstance: org.aula.app.rpc.quizzes.v1.GetQuizResponse by lazy { org.aula.app.rpc.quizzes.v1.GetQuizResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.quizzes.v1.GetQuizResponse = org.aula.app.rpc.quizzes.v1.GetQuizResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.quizzes.v1.GetQuizResponse> = pbandk.MessageDescriptor(
            fullName = "aula.quizzes.v1.GetQuizResponse",
            messageClass = org.aula.app.rpc.quizzes.v1.GetQuizResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quiz",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.quizzes.v1.Quiz.Companion),
                        jsonName = "quiz",
                        value = org.aula.app.rpc.quizzes.v1.GetQuizResponse::quiz
                    )
                )
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForQuizOption")
public fun QuizOption?.orDefault(): org.aula.app.rpc.quizzes.v1.QuizOption = this ?: QuizOption.defaultInstance

private fun QuizOption.protoMergeImpl(plus: pbandk.Message?): QuizOption = (plus as? QuizOption)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun QuizOption.Companion.decodeWithImpl(u: pbandk.MessageDecoder): QuizOption {
    var label = ""
    var text = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> label = _fieldValue as String
            2 -> text = _fieldValue as String
        }
    }

    return QuizOption(label, text, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForQuiz")
public fun Quiz?.orDefault(): org.aula.app.rpc.quizzes.v1.Quiz = this ?: Quiz.defaultInstance

private fun Quiz.protoMergeImpl(plus: pbandk.Message?): Quiz = (plus as? Quiz)?.let {
    it.copy(
        options = options + plus.options,
        closesAt = closesAt?.plus(plus.closesAt) ?: plus.closesAt,
        countsByLabel = countsByLabel + plus.countsByLabel,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Quiz.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Quiz {
    var id = ""
    var lessonId = ""
    var status: org.aula.app.rpc.quizzes.v1.QuizStatus = org.aula.app.rpc.quizzes.v1.QuizStatus.fromValue(0)
    var question = ""
    var options: pbandk.ListWithSize.Builder<org.aula.app.rpc.quizzes.v1.QuizOption>? = null
    var rationale = ""
    var slideNumber = 0
    var timeLimitSeconds = 0
    var closesAt: pbandk.wkt.Timestamp? = null
    var answeredCount = 0
    var enrolledCount = 0
    var correctLabel = ""
    var countsByLabel: pbandk.MessageMap.Builder<String, Int>? = null
    var correctPercent = 0
    var insight = ""
    var myLabel = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> lessonId = _fieldValue as String
            3 -> status = _fieldValue as org.aula.app.rpc.quizzes.v1.QuizStatus
            4 -> question = _fieldValue as String
            5 -> options = (options ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.quizzes.v1.QuizOption> }
            6 -> rationale = _fieldValue as String
            7 -> slideNumber = _fieldValue as Int
            8 -> timeLimitSeconds = _fieldValue as Int
            9 -> closesAt = _fieldValue as pbandk.wkt.Timestamp
            10 -> answeredCount = _fieldValue as Int
            11 -> enrolledCount = _fieldValue as Int
            12 -> correctLabel = _fieldValue as String
            13 -> countsByLabel = (countsByLabel ?: pbandk.MessageMap.Builder()).apply { this.entries += _fieldValue as kotlin.sequences.Sequence<pbandk.MessageMap.Entry<String, Int>> }
            14 -> correctPercent = _fieldValue as Int
            15 -> insight = _fieldValue as String
            16 -> myLabel = _fieldValue as String
        }
    }

    return Quiz(id, lessonId, status, question,
        pbandk.ListWithSize.Builder.fixed(options), rationale, slideNumber, timeLimitSeconds,
        closesAt, answeredCount, enrolledCount, correctLabel,
        pbandk.MessageMap.Builder.fixed(countsByLabel), correctPercent, insight, myLabel, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForQuizCountsByLabelEntry")
public fun Quiz.CountsByLabelEntry?.orDefault(): org.aula.app.rpc.quizzes.v1.Quiz.CountsByLabelEntry = this ?: Quiz.CountsByLabelEntry.defaultInstance

private fun Quiz.CountsByLabelEntry.protoMergeImpl(plus: pbandk.Message?): Quiz.CountsByLabelEntry = (plus as? Quiz.CountsByLabelEntry)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Quiz.CountsByLabelEntry.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Quiz.CountsByLabelEntry {
    var key = ""
    var value = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> key = _fieldValue as String
            2 -> value = _fieldValue as Int
        }
    }

    return Quiz.CountsByLabelEntry(key, value, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForDraftQuizRequest")
public fun DraftQuizRequest?.orDefault(): org.aula.app.rpc.quizzes.v1.DraftQuizRequest = this ?: DraftQuizRequest.defaultInstance

private fun DraftQuizRequest.protoMergeImpl(plus: pbandk.Message?): DraftQuizRequest = (plus as? DraftQuizRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun DraftQuizRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): DraftQuizRequest {
    var lessonId = ""
    var slideNumber = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
            2 -> slideNumber = _fieldValue as Int
        }
    }

    return DraftQuizRequest(lessonId, slideNumber, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForDraftQuizResponse")
public fun DraftQuizResponse?.orDefault(): org.aula.app.rpc.quizzes.v1.DraftQuizResponse = this ?: DraftQuizResponse.defaultInstance

private fun DraftQuizResponse.protoMergeImpl(plus: pbandk.Message?): DraftQuizResponse = (plus as? DraftQuizResponse)?.let {
    it.copy(
        quiz = quiz?.plus(plus.quiz) ?: plus.quiz,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun DraftQuizResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): DraftQuizResponse {
    var quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quiz = _fieldValue as org.aula.app.rpc.quizzes.v1.Quiz
        }
    }

    return DraftQuizResponse(quiz, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRegenerateQuizRequest")
public fun RegenerateQuizRequest?.orDefault(): org.aula.app.rpc.quizzes.v1.RegenerateQuizRequest = this ?: RegenerateQuizRequest.defaultInstance

private fun RegenerateQuizRequest.protoMergeImpl(plus: pbandk.Message?): RegenerateQuizRequest = (plus as? RegenerateQuizRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RegenerateQuizRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RegenerateQuizRequest {
    var quizId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quizId = _fieldValue as String
        }
    }

    return RegenerateQuizRequest(quizId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRegenerateQuizResponse")
public fun RegenerateQuizResponse?.orDefault(): org.aula.app.rpc.quizzes.v1.RegenerateQuizResponse = this ?: RegenerateQuizResponse.defaultInstance

private fun RegenerateQuizResponse.protoMergeImpl(plus: pbandk.Message?): RegenerateQuizResponse = (plus as? RegenerateQuizResponse)?.let {
    it.copy(
        quiz = quiz?.plus(plus.quiz) ?: plus.quiz,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RegenerateQuizResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RegenerateQuizResponse {
    var quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quiz = _fieldValue as org.aula.app.rpc.quizzes.v1.Quiz
        }
    }

    return RegenerateQuizResponse(quiz, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForUpdateQuizTimeRequest")
public fun UpdateQuizTimeRequest?.orDefault(): org.aula.app.rpc.quizzes.v1.UpdateQuizTimeRequest = this ?: UpdateQuizTimeRequest.defaultInstance

private fun UpdateQuizTimeRequest.protoMergeImpl(plus: pbandk.Message?): UpdateQuizTimeRequest = (plus as? UpdateQuizTimeRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun UpdateQuizTimeRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): UpdateQuizTimeRequest {
    var quizId = ""
    var timeLimitSeconds = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quizId = _fieldValue as String
            2 -> timeLimitSeconds = _fieldValue as Int
        }
    }

    return UpdateQuizTimeRequest(quizId, timeLimitSeconds, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForUpdateQuizTimeResponse")
public fun UpdateQuizTimeResponse?.orDefault(): org.aula.app.rpc.quizzes.v1.UpdateQuizTimeResponse = this ?: UpdateQuizTimeResponse.defaultInstance

private fun UpdateQuizTimeResponse.protoMergeImpl(plus: pbandk.Message?): UpdateQuizTimeResponse = (plus as? UpdateQuizTimeResponse)?.let {
    it.copy(
        quiz = quiz?.plus(plus.quiz) ?: plus.quiz,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun UpdateQuizTimeResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): UpdateQuizTimeResponse {
    var quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quiz = _fieldValue as org.aula.app.rpc.quizzes.v1.Quiz
        }
    }

    return UpdateQuizTimeResponse(quiz, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLaunchQuizRequest")
public fun LaunchQuizRequest?.orDefault(): org.aula.app.rpc.quizzes.v1.LaunchQuizRequest = this ?: LaunchQuizRequest.defaultInstance

private fun LaunchQuizRequest.protoMergeImpl(plus: pbandk.Message?): LaunchQuizRequest = (plus as? LaunchQuizRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LaunchQuizRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LaunchQuizRequest {
    var quizId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quizId = _fieldValue as String
        }
    }

    return LaunchQuizRequest(quizId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLaunchQuizResponse")
public fun LaunchQuizResponse?.orDefault(): org.aula.app.rpc.quizzes.v1.LaunchQuizResponse = this ?: LaunchQuizResponse.defaultInstance

private fun LaunchQuizResponse.protoMergeImpl(plus: pbandk.Message?): LaunchQuizResponse = (plus as? LaunchQuizResponse)?.let {
    it.copy(
        quiz = quiz?.plus(plus.quiz) ?: plus.quiz,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LaunchQuizResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LaunchQuizResponse {
    var quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quiz = _fieldValue as org.aula.app.rpc.quizzes.v1.Quiz
        }
    }

    return LaunchQuizResponse(quiz, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForAnswerQuizRequest")
public fun AnswerQuizRequest?.orDefault(): org.aula.app.rpc.quizzes.v1.AnswerQuizRequest = this ?: AnswerQuizRequest.defaultInstance

private fun AnswerQuizRequest.protoMergeImpl(plus: pbandk.Message?): AnswerQuizRequest = (plus as? AnswerQuizRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun AnswerQuizRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): AnswerQuizRequest {
    var quizId = ""
    var label = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quizId = _fieldValue as String
            2 -> label = _fieldValue as String
        }
    }

    return AnswerQuizRequest(quizId, label, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForAnswerQuizResponse")
public fun AnswerQuizResponse?.orDefault(): org.aula.app.rpc.quizzes.v1.AnswerQuizResponse = this ?: AnswerQuizResponse.defaultInstance

private fun AnswerQuizResponse.protoMergeImpl(plus: pbandk.Message?): AnswerQuizResponse = (plus as? AnswerQuizResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun AnswerQuizResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): AnswerQuizResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return AnswerQuizResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseQuizRequest")
public fun CloseQuizRequest?.orDefault(): org.aula.app.rpc.quizzes.v1.CloseQuizRequest = this ?: CloseQuizRequest.defaultInstance

private fun CloseQuizRequest.protoMergeImpl(plus: pbandk.Message?): CloseQuizRequest = (plus as? CloseQuizRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseQuizRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseQuizRequest {
    var quizId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quizId = _fieldValue as String
        }
    }

    return CloseQuizRequest(quizId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseQuizResponse")
public fun CloseQuizResponse?.orDefault(): org.aula.app.rpc.quizzes.v1.CloseQuizResponse = this ?: CloseQuizResponse.defaultInstance

private fun CloseQuizResponse.protoMergeImpl(plus: pbandk.Message?): CloseQuizResponse = (plus as? CloseQuizResponse)?.let {
    it.copy(
        quiz = quiz?.plus(plus.quiz) ?: plus.quiz,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseQuizResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseQuizResponse {
    var quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quiz = _fieldValue as org.aula.app.rpc.quizzes.v1.Quiz
        }
    }

    return CloseQuizResponse(quiz, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetQuizRequest")
public fun GetQuizRequest?.orDefault(): org.aula.app.rpc.quizzes.v1.GetQuizRequest = this ?: GetQuizRequest.defaultInstance

private fun GetQuizRequest.protoMergeImpl(plus: pbandk.Message?): GetQuizRequest = (plus as? GetQuizRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetQuizRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetQuizRequest {
    var quizId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quizId = _fieldValue as String
        }
    }

    return GetQuizRequest(quizId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetQuizResponse")
public fun GetQuizResponse?.orDefault(): org.aula.app.rpc.quizzes.v1.GetQuizResponse = this ?: GetQuizResponse.defaultInstance

private fun GetQuizResponse.protoMergeImpl(plus: pbandk.Message?): GetQuizResponse = (plus as? GetQuizResponse)?.let {
    it.copy(
        quiz = quiz?.plus(plus.quiz) ?: plus.quiz,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetQuizResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetQuizResponse {
    var quiz: org.aula.app.rpc.quizzes.v1.Quiz? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> quiz = _fieldValue as org.aula.app.rpc.quizzes.v1.Quiz
        }
    }

    return GetQuizResponse(quiz, unknownFields)
}
