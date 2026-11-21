@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.followup.v1

@pbandk.Export
public data class LessonInteractions(
    val number: Int = 0,
    val interactions: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.followup.v1.LessonInteractions = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.LessonInteractions> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.followup.v1.LessonInteractions> {
        public val defaultInstance: org.aula.app.rpc.followup.v1.LessonInteractions by lazy { org.aula.app.rpc.followup.v1.LessonInteractions() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.followup.v1.LessonInteractions = org.aula.app.rpc.followup.v1.LessonInteractions.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.LessonInteractions> = pbandk.MessageDescriptor(
            fullName = "aula.followup.v1.LessonInteractions",
            messageClass = org.aula.app.rpc.followup.v1.LessonInteractions::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "number",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "number",
                        value = org.aula.app.rpc.followup.v1.LessonInteractions::number
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "interactions",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "interactions",
                        value = org.aula.app.rpc.followup.v1.LessonInteractions::interactions
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetStudentParticipationRequest(
    val courseId: String = "",
    val studentId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.followup.v1.GetStudentParticipationRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.GetStudentParticipationRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.followup.v1.GetStudentParticipationRequest> {
        public val defaultInstance: org.aula.app.rpc.followup.v1.GetStudentParticipationRequest by lazy { org.aula.app.rpc.followup.v1.GetStudentParticipationRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.followup.v1.GetStudentParticipationRequest = org.aula.app.rpc.followup.v1.GetStudentParticipationRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.GetStudentParticipationRequest> = pbandk.MessageDescriptor(
            fullName = "aula.followup.v1.GetStudentParticipationRequest",
            messageClass = org.aula.app.rpc.followup.v1.GetStudentParticipationRequest::class,
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
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationRequest::courseId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "studentId",
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
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationRequest::studentId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetStudentParticipationResponse(
    val studentId: String = "",
    val name: String = "",
    val lessonsAttended: Int = 0,
    val lessonsTotal: Int = 0,
    val doubtsSent: Int = 0,
    val turnsTaken: Int = 0,
    val turnsCorrect: Int = 0,
    val quizzesAnswered: Int = 0,
    val quizzesTotal: Int = 0,
    val perLesson: List<org.aula.app.rpc.followup.v1.LessonInteractions> = emptyList(),
    val note: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.followup.v1.GetStudentParticipationResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.GetStudentParticipationResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.followup.v1.GetStudentParticipationResponse> {
        public val defaultInstance: org.aula.app.rpc.followup.v1.GetStudentParticipationResponse by lazy { org.aula.app.rpc.followup.v1.GetStudentParticipationResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.followup.v1.GetStudentParticipationResponse = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.GetStudentParticipationResponse> = pbandk.MessageDescriptor(
            fullName = "aula.followup.v1.GetStudentParticipationResponse",
            messageClass = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::class,
            messageCompanion = this,
            fields = buildList(11) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "studentId",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::studentId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lessons_attended",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "lessonsAttended",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::lessonsAttended
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lessons_total",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "lessonsTotal",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::lessonsTotal
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "doubts_sent",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "doubtsSent",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::doubtsSent
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "turns_taken",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "turnsTaken",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::turnsTaken
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "turns_correct",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "turnsCorrect",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::turnsCorrect
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quizzes_answered",
                        number = 8,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "quizzesAnswered",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::quizzesAnswered
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "quizzes_total",
                        number = 9,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "quizzesTotal",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::quizzesTotal
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "per_lesson",
                        number = 10,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.followup.v1.LessonInteractions>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.followup.v1.LessonInteractions.Companion)),
                        jsonName = "perLesson",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::perLesson
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "note",
                        number = 11,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "note",
                        value = org.aula.app.rpc.followup.v1.GetStudentParticipationResponse::note
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class SaveStudentNoteRequest(
    val courseId: String = "",
    val studentId: String = "",
    val body: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.followup.v1.SaveStudentNoteRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.SaveStudentNoteRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.followup.v1.SaveStudentNoteRequest> {
        public val defaultInstance: org.aula.app.rpc.followup.v1.SaveStudentNoteRequest by lazy { org.aula.app.rpc.followup.v1.SaveStudentNoteRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.followup.v1.SaveStudentNoteRequest = org.aula.app.rpc.followup.v1.SaveStudentNoteRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.SaveStudentNoteRequest> = pbandk.MessageDescriptor(
            fullName = "aula.followup.v1.SaveStudentNoteRequest",
            messageClass = org.aula.app.rpc.followup.v1.SaveStudentNoteRequest::class,
            messageCompanion = this,
            fields = buildList(3) {
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
                        value = org.aula.app.rpc.followup.v1.SaveStudentNoteRequest::courseId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "studentId",
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
                        value = org.aula.app.rpc.followup.v1.SaveStudentNoteRequest::studentId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "body",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "body",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(5, 114, 3, 24, -48, 15))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.followup.v1.SaveStudentNoteRequest::body
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class SaveStudentNoteResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.followup.v1.SaveStudentNoteResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.SaveStudentNoteResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.followup.v1.SaveStudentNoteResponse> {
        public val defaultInstance: org.aula.app.rpc.followup.v1.SaveStudentNoteResponse by lazy { org.aula.app.rpc.followup.v1.SaveStudentNoteResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.followup.v1.SaveStudentNoteResponse = org.aula.app.rpc.followup.v1.SaveStudentNoteResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.followup.v1.SaveStudentNoteResponse> = pbandk.MessageDescriptor(
            fullName = "aula.followup.v1.SaveStudentNoteResponse",
            messageClass = org.aula.app.rpc.followup.v1.SaveStudentNoteResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForLessonInteractions")
public fun LessonInteractions?.orDefault(): org.aula.app.rpc.followup.v1.LessonInteractions = this ?: LessonInteractions.defaultInstance

private fun LessonInteractions.protoMergeImpl(plus: pbandk.Message?): LessonInteractions = (plus as? LessonInteractions)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LessonInteractions.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LessonInteractions {
    var number = 0
    var interactions = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> number = _fieldValue as Int
            2 -> interactions = _fieldValue as Int
        }
    }

    return LessonInteractions(number, interactions, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetStudentParticipationRequest")
public fun GetStudentParticipationRequest?.orDefault(): org.aula.app.rpc.followup.v1.GetStudentParticipationRequest = this ?: GetStudentParticipationRequest.defaultInstance

private fun GetStudentParticipationRequest.protoMergeImpl(plus: pbandk.Message?): GetStudentParticipationRequest = (plus as? GetStudentParticipationRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetStudentParticipationRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetStudentParticipationRequest {
    var courseId = ""
    var studentId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> courseId = _fieldValue as String
            2 -> studentId = _fieldValue as String
        }
    }

    return GetStudentParticipationRequest(courseId, studentId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetStudentParticipationResponse")
public fun GetStudentParticipationResponse?.orDefault(): org.aula.app.rpc.followup.v1.GetStudentParticipationResponse = this ?: GetStudentParticipationResponse.defaultInstance

private fun GetStudentParticipationResponse.protoMergeImpl(plus: pbandk.Message?): GetStudentParticipationResponse = (plus as? GetStudentParticipationResponse)?.let {
    it.copy(
        perLesson = perLesson + plus.perLesson,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetStudentParticipationResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetStudentParticipationResponse {
    var studentId = ""
    var name = ""
    var lessonsAttended = 0
    var lessonsTotal = 0
    var doubtsSent = 0
    var turnsTaken = 0
    var turnsCorrect = 0
    var quizzesAnswered = 0
    var quizzesTotal = 0
    var perLesson: pbandk.ListWithSize.Builder<org.aula.app.rpc.followup.v1.LessonInteractions>? = null
    var note = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> studentId = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> lessonsAttended = _fieldValue as Int
            4 -> lessonsTotal = _fieldValue as Int
            5 -> doubtsSent = _fieldValue as Int
            6 -> turnsTaken = _fieldValue as Int
            7 -> turnsCorrect = _fieldValue as Int
            8 -> quizzesAnswered = _fieldValue as Int
            9 -> quizzesTotal = _fieldValue as Int
            10 -> perLesson = (perLesson ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.followup.v1.LessonInteractions> }
            11 -> note = _fieldValue as String
        }
    }

    return GetStudentParticipationResponse(studentId, name, lessonsAttended, lessonsTotal,
        doubtsSent, turnsTaken, turnsCorrect, quizzesAnswered,
        quizzesTotal, pbandk.ListWithSize.Builder.fixed(perLesson), note, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSaveStudentNoteRequest")
public fun SaveStudentNoteRequest?.orDefault(): org.aula.app.rpc.followup.v1.SaveStudentNoteRequest = this ?: SaveStudentNoteRequest.defaultInstance

private fun SaveStudentNoteRequest.protoMergeImpl(plus: pbandk.Message?): SaveStudentNoteRequest = (plus as? SaveStudentNoteRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun SaveStudentNoteRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): SaveStudentNoteRequest {
    var courseId = ""
    var studentId = ""
    var body = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> courseId = _fieldValue as String
            2 -> studentId = _fieldValue as String
            3 -> body = _fieldValue as String
        }
    }

    return SaveStudentNoteRequest(courseId, studentId, body, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSaveStudentNoteResponse")
public fun SaveStudentNoteResponse?.orDefault(): org.aula.app.rpc.followup.v1.SaveStudentNoteResponse = this ?: SaveStudentNoteResponse.defaultInstance

private fun SaveStudentNoteResponse.protoMergeImpl(plus: pbandk.Message?): SaveStudentNoteResponse = (plus as? SaveStudentNoteResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun SaveStudentNoteResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): SaveStudentNoteResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return SaveStudentNoteResponse(unknownFields)
}
