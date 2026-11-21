@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.doubts.v1

@pbandk.Export
public sealed class DoubtRoundStatus(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.doubts.v1.DoubtRoundStatus && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "DoubtRoundStatus.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : DoubtRoundStatus(0, "DOUBT_ROUND_STATUS_UNSPECIFIED")
    public object OPEN : DoubtRoundStatus(1, "DOUBT_ROUND_STATUS_OPEN")
    public object CLOSED : DoubtRoundStatus(2, "DOUBT_ROUND_STATUS_CLOSED")
    public class UNRECOGNIZED(value: Int) : DoubtRoundStatus(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.doubts.v1.DoubtRoundStatus> {
        public val values: List<org.aula.app.rpc.doubts.v1.DoubtRoundStatus> by lazy { listOf(UNSPECIFIED, OPEN, CLOSED) }
        override fun fromValue(value: Int): org.aula.app.rpc.doubts.v1.DoubtRoundStatus = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.doubts.v1.DoubtRoundStatus = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No DoubtRoundStatus with name: $name")
    }
}

@pbandk.Export
public sealed class SummaryStatus(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.doubts.v1.SummaryStatus && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "SummaryStatus.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : SummaryStatus(0, "SUMMARY_STATUS_UNSPECIFIED")
    public object PENDING : SummaryStatus(1, "SUMMARY_STATUS_PENDING")
    public object READY : SummaryStatus(2, "SUMMARY_STATUS_READY")
    public object FAILED : SummaryStatus(3, "SUMMARY_STATUS_FAILED")
    public class UNRECOGNIZED(value: Int) : SummaryStatus(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.doubts.v1.SummaryStatus> {
        public val values: List<org.aula.app.rpc.doubts.v1.SummaryStatus> by lazy { listOf(UNSPECIFIED, PENDING, READY, FAILED) }
        override fun fromValue(value: Int): org.aula.app.rpc.doubts.v1.SummaryStatus = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.doubts.v1.SummaryStatus = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No SummaryStatus with name: $name")
    }
}

@pbandk.Export
public data class DoubtGroup(
    val id: String = "",
    val rank: Int = 0,
    val title: String = "",
    val slideNumber: Int = 0,
    val studentCount: Int = 0,
    val sampleQuote: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.DoubtGroup = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.DoubtGroup> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.DoubtGroup> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.DoubtGroup by lazy { org.aula.app.rpc.doubts.v1.DoubtGroup() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.DoubtGroup = org.aula.app.rpc.doubts.v1.DoubtGroup.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.DoubtGroup> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.DoubtGroup",
            messageClass = org.aula.app.rpc.doubts.v1.DoubtGroup::class,
            messageCompanion = this,
            fields = buildList(6) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.doubts.v1.DoubtGroup::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "rank",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "rank",
                        value = org.aula.app.rpc.doubts.v1.DoubtGroup::rank
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "title",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "title",
                        value = org.aula.app.rpc.doubts.v1.DoubtGroup::title
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "slide_number",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "slideNumber",
                        value = org.aula.app.rpc.doubts.v1.DoubtGroup::slideNumber
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_count",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "studentCount",
                        value = org.aula.app.rpc.doubts.v1.DoubtGroup::studentCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "sample_quote",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "sampleQuote",
                        value = org.aula.app.rpc.doubts.v1.DoubtGroup::sampleQuote
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class MyDoubt(
    val text: String = "",
    val editableUntil: pbandk.wkt.Timestamp? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.MyDoubt = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.MyDoubt> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.MyDoubt> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.MyDoubt by lazy { org.aula.app.rpc.doubts.v1.MyDoubt() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.MyDoubt = org.aula.app.rpc.doubts.v1.MyDoubt.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.MyDoubt> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.MyDoubt",
            messageClass = org.aula.app.rpc.doubts.v1.MyDoubt::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "text",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "text",
                        value = org.aula.app.rpc.doubts.v1.MyDoubt::text
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "editable_until",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "editableUntil",
                        value = org.aula.app.rpc.doubts.v1.MyDoubt::editableUntil
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class DoubtRound(
    val id: String = "",
    val lessonId: String = "",
    val status: org.aula.app.rpc.doubts.v1.DoubtRoundStatus = org.aula.app.rpc.doubts.v1.DoubtRoundStatus.fromValue(0),
    val closesAt: pbandk.wkt.Timestamp? = null,
    val submittedCount: Int = 0,
    val enrolledCount: Int = 0,
    val summaryStatus: org.aula.app.rpc.doubts.v1.SummaryStatus = org.aula.app.rpc.doubts.v1.SummaryStatus.fromValue(0),
    val groups: List<org.aula.app.rpc.doubts.v1.DoubtGroup> = emptyList(),
    val myDoubt: org.aula.app.rpc.doubts.v1.MyDoubt? = null,
    val myGroup: org.aula.app.rpc.doubts.v1.DoubtGroup? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.DoubtRound = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.DoubtRound> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.DoubtRound> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.DoubtRound by lazy { org.aula.app.rpc.doubts.v1.DoubtRound() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.DoubtRound = org.aula.app.rpc.doubts.v1.DoubtRound.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.DoubtRound> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.DoubtRound",
            messageClass = org.aula.app.rpc.doubts.v1.DoubtRound::class,
            messageCompanion = this,
            fields = buildList(10) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.doubts.v1.DoubtRoundStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::status
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "closes_at",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "closesAt",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::closesAt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "submitted_count",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "submittedCount",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::submittedCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "enrolled_count",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "enrolledCount",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::enrolledCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "summary_status",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.doubts.v1.SummaryStatus.Companion),
                        jsonName = "summaryStatus",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::summaryStatus
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "groups",
                        number = 8,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.doubts.v1.DoubtGroup>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.DoubtGroup.Companion)),
                        jsonName = "groups",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::groups
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "my_doubt",
                        number = 9,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.MyDoubt.Companion),
                        jsonName = "myDoubt",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::myDoubt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "my_group",
                        number = 10,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.DoubtGroup.Companion),
                        jsonName = "myGroup",
                        value = org.aula.app.rpc.doubts.v1.DoubtRound::myGroup
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenDoubtRoundRequest(
    val lessonId: String = "",
    val durationSeconds: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest by lazy { org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest = org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.OpenDoubtRoundRequest",
            messageClass = org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest::class,
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
                        value = org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "duration_seconds",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "durationSeconds",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 26, 5, 24, -84, 2, 40, 0))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest::durationSeconds
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenDoubtRoundResponse(
    val round: org.aula.app.rpc.doubts.v1.DoubtRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse by lazy { org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse = org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.OpenDoubtRoundResponse",
            messageClass = org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.DoubtRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ExtendDoubtRoundRequest(
    val roundId: String = "",
    val seconds: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest by lazy { org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest = org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.ExtendDoubtRoundRequest",
            messageClass = org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "roundId",
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
                        value = org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest::roundId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "seconds",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "seconds",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(6, 26, 4, 24, 120, 40, 0))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest::seconds
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ExtendDoubtRoundResponse(
    val round: org.aula.app.rpc.doubts.v1.DoubtRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse by lazy { org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse = org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.ExtendDoubtRoundResponse",
            messageClass = org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.DoubtRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseDoubtRoundRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest by lazy { org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest = org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.CloseDoubtRoundRequest",
            messageClass = org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "roundId",
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
                        value = org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseDoubtRoundResponse(
    val round: org.aula.app.rpc.doubts.v1.DoubtRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse by lazy { org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse = org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.CloseDoubtRoundResponse",
            messageClass = org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.DoubtRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class SubmitDoubtRequest(
    val roundId: String = "",
    val text: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.SubmitDoubtRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.SubmitDoubtRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.SubmitDoubtRequest> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.SubmitDoubtRequest by lazy { org.aula.app.rpc.doubts.v1.SubmitDoubtRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.SubmitDoubtRequest = org.aula.app.rpc.doubts.v1.SubmitDoubtRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.SubmitDoubtRequest> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.SubmitDoubtRequest",
            messageClass = org.aula.app.rpc.doubts.v1.SubmitDoubtRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "roundId",
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
                        value = org.aula.app.rpc.doubts.v1.SubmitDoubtRequest::roundId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "text",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "text",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 114, 5, 16, 1, 24, -12, 3))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.doubts.v1.SubmitDoubtRequest::text
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class SubmitDoubtResponse(
    val doubt: org.aula.app.rpc.doubts.v1.MyDoubt? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.SubmitDoubtResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.SubmitDoubtResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.SubmitDoubtResponse> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.SubmitDoubtResponse by lazy { org.aula.app.rpc.doubts.v1.SubmitDoubtResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.SubmitDoubtResponse = org.aula.app.rpc.doubts.v1.SubmitDoubtResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.SubmitDoubtResponse> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.SubmitDoubtResponse",
            messageClass = org.aula.app.rpc.doubts.v1.SubmitDoubtResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "doubt",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.MyDoubt.Companion),
                        jsonName = "doubt",
                        value = org.aula.app.rpc.doubts.v1.SubmitDoubtResponse::doubt
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetDoubtRoundRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest by lazy { org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest = org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.GetDoubtRoundRequest",
            messageClass = org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "roundId",
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
                        value = org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetDoubtRoundResponse(
    val round: org.aula.app.rpc.doubts.v1.DoubtRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse by lazy { org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse = org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.GetDoubtRoundResponse",
            messageClass = org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.DoubtRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OriginalDoubt(
    val id: String = "",
    val text: String = "",
    val groupId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.OriginalDoubt = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.OriginalDoubt> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.OriginalDoubt> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.OriginalDoubt by lazy { org.aula.app.rpc.doubts.v1.OriginalDoubt() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.OriginalDoubt = org.aula.app.rpc.doubts.v1.OriginalDoubt.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.OriginalDoubt> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.OriginalDoubt",
            messageClass = org.aula.app.rpc.doubts.v1.OriginalDoubt::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.doubts.v1.OriginalDoubt::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "text",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "text",
                        value = org.aula.app.rpc.doubts.v1.OriginalDoubt::text
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "group_id",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "groupId",
                        value = org.aula.app.rpc.doubts.v1.OriginalDoubt::groupId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ListDoubtsRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.ListDoubtsRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.ListDoubtsRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.ListDoubtsRequest> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.ListDoubtsRequest by lazy { org.aula.app.rpc.doubts.v1.ListDoubtsRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.ListDoubtsRequest = org.aula.app.rpc.doubts.v1.ListDoubtsRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.ListDoubtsRequest> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.ListDoubtsRequest",
            messageClass = org.aula.app.rpc.doubts.v1.ListDoubtsRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "roundId",
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
                        value = org.aula.app.rpc.doubts.v1.ListDoubtsRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ListDoubtsResponse(
    val doubts: List<org.aula.app.rpc.doubts.v1.OriginalDoubt> = emptyList(),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.doubts.v1.ListDoubtsResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.ListDoubtsResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.doubts.v1.ListDoubtsResponse> {
        public val defaultInstance: org.aula.app.rpc.doubts.v1.ListDoubtsResponse by lazy { org.aula.app.rpc.doubts.v1.ListDoubtsResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.doubts.v1.ListDoubtsResponse = org.aula.app.rpc.doubts.v1.ListDoubtsResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.doubts.v1.ListDoubtsResponse> = pbandk.MessageDescriptor(
            fullName = "aula.doubts.v1.ListDoubtsResponse",
            messageClass = org.aula.app.rpc.doubts.v1.ListDoubtsResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "doubts",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.doubts.v1.OriginalDoubt>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.doubts.v1.OriginalDoubt.Companion)),
                        jsonName = "doubts",
                        value = org.aula.app.rpc.doubts.v1.ListDoubtsResponse::doubts
                    )
                )
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForDoubtGroup")
public fun DoubtGroup?.orDefault(): org.aula.app.rpc.doubts.v1.DoubtGroup = this ?: DoubtGroup.defaultInstance

private fun DoubtGroup.protoMergeImpl(plus: pbandk.Message?): DoubtGroup = (plus as? DoubtGroup)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun DoubtGroup.Companion.decodeWithImpl(u: pbandk.MessageDecoder): DoubtGroup {
    var id = ""
    var rank = 0
    var title = ""
    var slideNumber = 0
    var studentCount = 0
    var sampleQuote = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> rank = _fieldValue as Int
            3 -> title = _fieldValue as String
            4 -> slideNumber = _fieldValue as Int
            5 -> studentCount = _fieldValue as Int
            6 -> sampleQuote = _fieldValue as String
        }
    }

    return DoubtGroup(id, rank, title, slideNumber,
        studentCount, sampleQuote, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForMyDoubt")
public fun MyDoubt?.orDefault(): org.aula.app.rpc.doubts.v1.MyDoubt = this ?: MyDoubt.defaultInstance

private fun MyDoubt.protoMergeImpl(plus: pbandk.Message?): MyDoubt = (plus as? MyDoubt)?.let {
    it.copy(
        editableUntil = editableUntil?.plus(plus.editableUntil) ?: plus.editableUntil,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun MyDoubt.Companion.decodeWithImpl(u: pbandk.MessageDecoder): MyDoubt {
    var text = ""
    var editableUntil: pbandk.wkt.Timestamp? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> text = _fieldValue as String
            2 -> editableUntil = _fieldValue as pbandk.wkt.Timestamp
        }
    }

    return MyDoubt(text, editableUntil, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForDoubtRound")
public fun DoubtRound?.orDefault(): org.aula.app.rpc.doubts.v1.DoubtRound = this ?: DoubtRound.defaultInstance

private fun DoubtRound.protoMergeImpl(plus: pbandk.Message?): DoubtRound = (plus as? DoubtRound)?.let {
    it.copy(
        closesAt = closesAt?.plus(plus.closesAt) ?: plus.closesAt,
        groups = groups + plus.groups,
        myDoubt = myDoubt?.plus(plus.myDoubt) ?: plus.myDoubt,
        myGroup = myGroup?.plus(plus.myGroup) ?: plus.myGroup,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun DoubtRound.Companion.decodeWithImpl(u: pbandk.MessageDecoder): DoubtRound {
    var id = ""
    var lessonId = ""
    var status: org.aula.app.rpc.doubts.v1.DoubtRoundStatus = org.aula.app.rpc.doubts.v1.DoubtRoundStatus.fromValue(0)
    var closesAt: pbandk.wkt.Timestamp? = null
    var submittedCount = 0
    var enrolledCount = 0
    var summaryStatus: org.aula.app.rpc.doubts.v1.SummaryStatus = org.aula.app.rpc.doubts.v1.SummaryStatus.fromValue(0)
    var groups: pbandk.ListWithSize.Builder<org.aula.app.rpc.doubts.v1.DoubtGroup>? = null
    var myDoubt: org.aula.app.rpc.doubts.v1.MyDoubt? = null
    var myGroup: org.aula.app.rpc.doubts.v1.DoubtGroup? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> lessonId = _fieldValue as String
            3 -> status = _fieldValue as org.aula.app.rpc.doubts.v1.DoubtRoundStatus
            4 -> closesAt = _fieldValue as pbandk.wkt.Timestamp
            5 -> submittedCount = _fieldValue as Int
            6 -> enrolledCount = _fieldValue as Int
            7 -> summaryStatus = _fieldValue as org.aula.app.rpc.doubts.v1.SummaryStatus
            8 -> groups = (groups ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.doubts.v1.DoubtGroup> }
            9 -> myDoubt = _fieldValue as org.aula.app.rpc.doubts.v1.MyDoubt
            10 -> myGroup = _fieldValue as org.aula.app.rpc.doubts.v1.DoubtGroup
        }
    }

    return DoubtRound(id, lessonId, status, closesAt,
        submittedCount, enrolledCount, summaryStatus, pbandk.ListWithSize.Builder.fixed(groups),
        myDoubt, myGroup, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenDoubtRoundRequest")
public fun OpenDoubtRoundRequest?.orDefault(): org.aula.app.rpc.doubts.v1.OpenDoubtRoundRequest = this ?: OpenDoubtRoundRequest.defaultInstance

private fun OpenDoubtRoundRequest.protoMergeImpl(plus: pbandk.Message?): OpenDoubtRoundRequest = (plus as? OpenDoubtRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenDoubtRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenDoubtRoundRequest {
    var lessonId = ""
    var durationSeconds = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
            2 -> durationSeconds = _fieldValue as Int
        }
    }

    return OpenDoubtRoundRequest(lessonId, durationSeconds, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenDoubtRoundResponse")
public fun OpenDoubtRoundResponse?.orDefault(): org.aula.app.rpc.doubts.v1.OpenDoubtRoundResponse = this ?: OpenDoubtRoundResponse.defaultInstance

private fun OpenDoubtRoundResponse.protoMergeImpl(plus: pbandk.Message?): OpenDoubtRoundResponse = (plus as? OpenDoubtRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenDoubtRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenDoubtRoundResponse {
    var round: org.aula.app.rpc.doubts.v1.DoubtRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.doubts.v1.DoubtRound
        }
    }

    return OpenDoubtRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForExtendDoubtRoundRequest")
public fun ExtendDoubtRoundRequest?.orDefault(): org.aula.app.rpc.doubts.v1.ExtendDoubtRoundRequest = this ?: ExtendDoubtRoundRequest.defaultInstance

private fun ExtendDoubtRoundRequest.protoMergeImpl(plus: pbandk.Message?): ExtendDoubtRoundRequest = (plus as? ExtendDoubtRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ExtendDoubtRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ExtendDoubtRoundRequest {
    var roundId = ""
    var seconds = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
            2 -> seconds = _fieldValue as Int
        }
    }

    return ExtendDoubtRoundRequest(roundId, seconds, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForExtendDoubtRoundResponse")
public fun ExtendDoubtRoundResponse?.orDefault(): org.aula.app.rpc.doubts.v1.ExtendDoubtRoundResponse = this ?: ExtendDoubtRoundResponse.defaultInstance

private fun ExtendDoubtRoundResponse.protoMergeImpl(plus: pbandk.Message?): ExtendDoubtRoundResponse = (plus as? ExtendDoubtRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ExtendDoubtRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ExtendDoubtRoundResponse {
    var round: org.aula.app.rpc.doubts.v1.DoubtRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.doubts.v1.DoubtRound
        }
    }

    return ExtendDoubtRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseDoubtRoundRequest")
public fun CloseDoubtRoundRequest?.orDefault(): org.aula.app.rpc.doubts.v1.CloseDoubtRoundRequest = this ?: CloseDoubtRoundRequest.defaultInstance

private fun CloseDoubtRoundRequest.protoMergeImpl(plus: pbandk.Message?): CloseDoubtRoundRequest = (plus as? CloseDoubtRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseDoubtRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseDoubtRoundRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return CloseDoubtRoundRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseDoubtRoundResponse")
public fun CloseDoubtRoundResponse?.orDefault(): org.aula.app.rpc.doubts.v1.CloseDoubtRoundResponse = this ?: CloseDoubtRoundResponse.defaultInstance

private fun CloseDoubtRoundResponse.protoMergeImpl(plus: pbandk.Message?): CloseDoubtRoundResponse = (plus as? CloseDoubtRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseDoubtRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseDoubtRoundResponse {
    var round: org.aula.app.rpc.doubts.v1.DoubtRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.doubts.v1.DoubtRound
        }
    }

    return CloseDoubtRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSubmitDoubtRequest")
public fun SubmitDoubtRequest?.orDefault(): org.aula.app.rpc.doubts.v1.SubmitDoubtRequest = this ?: SubmitDoubtRequest.defaultInstance

private fun SubmitDoubtRequest.protoMergeImpl(plus: pbandk.Message?): SubmitDoubtRequest = (plus as? SubmitDoubtRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun SubmitDoubtRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): SubmitDoubtRequest {
    var roundId = ""
    var text = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
            2 -> text = _fieldValue as String
        }
    }

    return SubmitDoubtRequest(roundId, text, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSubmitDoubtResponse")
public fun SubmitDoubtResponse?.orDefault(): org.aula.app.rpc.doubts.v1.SubmitDoubtResponse = this ?: SubmitDoubtResponse.defaultInstance

private fun SubmitDoubtResponse.protoMergeImpl(plus: pbandk.Message?): SubmitDoubtResponse = (plus as? SubmitDoubtResponse)?.let {
    it.copy(
        doubt = doubt?.plus(plus.doubt) ?: plus.doubt,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun SubmitDoubtResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): SubmitDoubtResponse {
    var doubt: org.aula.app.rpc.doubts.v1.MyDoubt? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> doubt = _fieldValue as org.aula.app.rpc.doubts.v1.MyDoubt
        }
    }

    return SubmitDoubtResponse(doubt, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetDoubtRoundRequest")
public fun GetDoubtRoundRequest?.orDefault(): org.aula.app.rpc.doubts.v1.GetDoubtRoundRequest = this ?: GetDoubtRoundRequest.defaultInstance

private fun GetDoubtRoundRequest.protoMergeImpl(plus: pbandk.Message?): GetDoubtRoundRequest = (plus as? GetDoubtRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetDoubtRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetDoubtRoundRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return GetDoubtRoundRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetDoubtRoundResponse")
public fun GetDoubtRoundResponse?.orDefault(): org.aula.app.rpc.doubts.v1.GetDoubtRoundResponse = this ?: GetDoubtRoundResponse.defaultInstance

private fun GetDoubtRoundResponse.protoMergeImpl(plus: pbandk.Message?): GetDoubtRoundResponse = (plus as? GetDoubtRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetDoubtRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetDoubtRoundResponse {
    var round: org.aula.app.rpc.doubts.v1.DoubtRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.doubts.v1.DoubtRound
        }
    }

    return GetDoubtRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOriginalDoubt")
public fun OriginalDoubt?.orDefault(): org.aula.app.rpc.doubts.v1.OriginalDoubt = this ?: OriginalDoubt.defaultInstance

private fun OriginalDoubt.protoMergeImpl(plus: pbandk.Message?): OriginalDoubt = (plus as? OriginalDoubt)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OriginalDoubt.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OriginalDoubt {
    var id = ""
    var text = ""
    var groupId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> text = _fieldValue as String
            3 -> groupId = _fieldValue as String
        }
    }

    return OriginalDoubt(id, text, groupId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListDoubtsRequest")
public fun ListDoubtsRequest?.orDefault(): org.aula.app.rpc.doubts.v1.ListDoubtsRequest = this ?: ListDoubtsRequest.defaultInstance

private fun ListDoubtsRequest.protoMergeImpl(plus: pbandk.Message?): ListDoubtsRequest = (plus as? ListDoubtsRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListDoubtsRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListDoubtsRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return ListDoubtsRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListDoubtsResponse")
public fun ListDoubtsResponse?.orDefault(): org.aula.app.rpc.doubts.v1.ListDoubtsResponse = this ?: ListDoubtsResponse.defaultInstance

private fun ListDoubtsResponse.protoMergeImpl(plus: pbandk.Message?): ListDoubtsResponse = (plus as? ListDoubtsResponse)?.let {
    it.copy(
        doubts = doubts + plus.doubts,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListDoubtsResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListDoubtsResponse {
    var doubts: pbandk.ListWithSize.Builder<org.aula.app.rpc.doubts.v1.OriginalDoubt>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> doubts = (doubts ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.doubts.v1.OriginalDoubt> }
        }
    }

    return ListDoubtsResponse(pbandk.ListWithSize.Builder.fixed(doubts), unknownFields)
}
