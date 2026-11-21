@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.floor.v1

@pbandk.Export
public sealed class FloorRoundStatus(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.floor.v1.FloorRoundStatus && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "FloorRoundStatus.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : FloorRoundStatus(0, "FLOOR_ROUND_STATUS_UNSPECIFIED")
    public object OPEN : FloorRoundStatus(1, "FLOOR_ROUND_STATUS_OPEN")
    public object CLOSED : FloorRoundStatus(2, "FLOOR_ROUND_STATUS_CLOSED")
    public class UNRECOGNIZED(value: Int) : FloorRoundStatus(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.floor.v1.FloorRoundStatus> {
        public val values: List<org.aula.app.rpc.floor.v1.FloorRoundStatus> by lazy { listOf(UNSPECIFIED, OPEN, CLOSED) }
        override fun fromValue(value: Int): org.aula.app.rpc.floor.v1.FloorRoundStatus = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.floor.v1.FloorRoundStatus = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No FloorRoundStatus with name: $name")
    }
}

@pbandk.Export
public sealed class FloorTurnState(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.floor.v1.FloorTurnState && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "FloorTurnState.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : FloorTurnState(0, "FLOOR_TURN_STATE_UNSPECIFIED")
    public object QUEUED : FloorTurnState(1, "FLOOR_TURN_STATE_QUEUED")
    public object ANSWERING : FloorTurnState(2, "FLOOR_TURN_STATE_ANSWERING")
    public object ANSWERED : FloorTurnState(3, "FLOOR_TURN_STATE_ANSWERED")
    public object CORRECT : FloorTurnState(4, "FLOOR_TURN_STATE_CORRECT")
    public object INCORRECT : FloorTurnState(5, "FLOOR_TURN_STATE_INCORRECT")
    public object WITHDRAWN : FloorTurnState(6, "FLOOR_TURN_STATE_WITHDRAWN")
    public class UNRECOGNIZED(value: Int) : FloorTurnState(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.floor.v1.FloorTurnState> {
        public val values: List<org.aula.app.rpc.floor.v1.FloorTurnState> by lazy { listOf(UNSPECIFIED, QUEUED, ANSWERING, ANSWERED, CORRECT, INCORRECT, WITHDRAWN) }
        override fun fromValue(value: Int): org.aula.app.rpc.floor.v1.FloorTurnState = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.floor.v1.FloorTurnState = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No FloorTurnState with name: $name")
    }
}

@pbandk.Export
public sealed class TurnVerdict(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.floor.v1.TurnVerdict && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "TurnVerdict.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : TurnVerdict(0, "TURN_VERDICT_UNSPECIFIED")
    public object CORRECT : TurnVerdict(1, "TURN_VERDICT_CORRECT")
    public object INCORRECT : TurnVerdict(2, "TURN_VERDICT_INCORRECT")
    public class UNRECOGNIZED(value: Int) : TurnVerdict(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.floor.v1.TurnVerdict> {
        public val values: List<org.aula.app.rpc.floor.v1.TurnVerdict> by lazy { listOf(UNSPECIFIED, CORRECT, INCORRECT) }
        override fun fromValue(value: Int): org.aula.app.rpc.floor.v1.TurnVerdict = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.floor.v1.TurnVerdict = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No TurnVerdict with name: $name")
    }
}

@pbandk.Export
public data class Responder(
    val studentId: String = "",
    val name: String = "",
    val deadlineAt: pbandk.wkt.Timestamp? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.Responder = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.Responder> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.Responder> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.Responder by lazy { org.aula.app.rpc.floor.v1.Responder() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.Responder = org.aula.app.rpc.floor.v1.Responder.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.Responder> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.Responder",
            messageClass = org.aula.app.rpc.floor.v1.Responder::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "studentId",
                        value = org.aula.app.rpc.floor.v1.Responder::studentId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.floor.v1.Responder::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "deadline_at",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "deadlineAt",
                        value = org.aula.app.rpc.floor.v1.Responder::deadlineAt
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class QueueEntry(
    val studentId: String = "",
    val name: String = "",
    val offsetMs: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.QueueEntry = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.QueueEntry> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.QueueEntry> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.QueueEntry by lazy { org.aula.app.rpc.floor.v1.QueueEntry() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.QueueEntry = org.aula.app.rpc.floor.v1.QueueEntry.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.QueueEntry> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.QueueEntry",
            messageClass = org.aula.app.rpc.floor.v1.QueueEntry::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "studentId",
                        value = org.aula.app.rpc.floor.v1.QueueEntry::studentId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.floor.v1.QueueEntry::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "offset_ms",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "offsetMs",
                        value = org.aula.app.rpc.floor.v1.QueueEntry::offsetMs
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class FloorRound(
    val id: String = "",
    val lessonId: String = "",
    val prompt: String = "",
    val status: org.aula.app.rpc.floor.v1.FloorRoundStatus = org.aula.app.rpc.floor.v1.FloorRoundStatus.fromValue(0),
    val openToAll: Boolean = false,
    val current: org.aula.app.rpc.floor.v1.Responder? = null,
    val queue: List<org.aula.app.rpc.floor.v1.QueueEntry> = emptyList(),
    val myState: org.aula.app.rpc.floor.v1.FloorTurnState = org.aula.app.rpc.floor.v1.FloorTurnState.fromValue(0),
    val myPosition: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.FloorRound = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.FloorRound> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.FloorRound> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.FloorRound by lazy { org.aula.app.rpc.floor.v1.FloorRound() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.FloorRound = org.aula.app.rpc.floor.v1.FloorRound.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.FloorRound> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.FloorRound",
            messageClass = org.aula.app.rpc.floor.v1.FloorRound::class,
            messageCompanion = this,
            fields = buildList(9) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.floor.v1.FloorRound::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        value = org.aula.app.rpc.floor.v1.FloorRound::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "prompt",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "prompt",
                        value = org.aula.app.rpc.floor.v1.FloorRound::prompt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.floor.v1.FloorRoundStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.floor.v1.FloorRound::status
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "open_to_all",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "openToAll",
                        value = org.aula.app.rpc.floor.v1.FloorRound::openToAll
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "current",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.floor.v1.Responder.Companion),
                        jsonName = "current",
                        value = org.aula.app.rpc.floor.v1.FloorRound::current
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "queue",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.floor.v1.QueueEntry>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.floor.v1.QueueEntry.Companion)),
                        jsonName = "queue",
                        value = org.aula.app.rpc.floor.v1.FloorRound::queue
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "my_state",
                        number = 8,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.floor.v1.FloorTurnState.Companion),
                        jsonName = "myState",
                        value = org.aula.app.rpc.floor.v1.FloorRound::myState
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "my_position",
                        number = 9,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "myPosition",
                        value = org.aula.app.rpc.floor.v1.FloorRound::myPosition
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenFloorRoundRequest(
    val lessonId: String = "",
    val prompt: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.OpenFloorRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.OpenFloorRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.OpenFloorRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.OpenFloorRoundRequest by lazy { org.aula.app.rpc.floor.v1.OpenFloorRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.OpenFloorRoundRequest = org.aula.app.rpc.floor.v1.OpenFloorRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.OpenFloorRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.OpenFloorRoundRequest",
            messageClass = org.aula.app.rpc.floor.v1.OpenFloorRoundRequest::class,
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
                        value = org.aula.app.rpc.floor.v1.OpenFloorRoundRequest::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "prompt",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "prompt",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 114, 5, 16, 1, 24, -84, 2))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.floor.v1.OpenFloorRoundRequest::prompt
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenFloorRoundResponse(
    val round: org.aula.app.rpc.floor.v1.FloorRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.OpenFloorRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.OpenFloorRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.OpenFloorRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.OpenFloorRoundResponse by lazy { org.aula.app.rpc.floor.v1.OpenFloorRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.OpenFloorRoundResponse = org.aula.app.rpc.floor.v1.OpenFloorRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.OpenFloorRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.OpenFloorRoundResponse",
            messageClass = org.aula.app.rpc.floor.v1.OpenFloorRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.floor.v1.FloorRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.floor.v1.OpenFloorRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RaiseHandRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.RaiseHandRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.RaiseHandRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.RaiseHandRequest> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.RaiseHandRequest by lazy { org.aula.app.rpc.floor.v1.RaiseHandRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.RaiseHandRequest = org.aula.app.rpc.floor.v1.RaiseHandRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.RaiseHandRequest> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.RaiseHandRequest",
            messageClass = org.aula.app.rpc.floor.v1.RaiseHandRequest::class,
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
                        value = org.aula.app.rpc.floor.v1.RaiseHandRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RaiseHandResponse(
    val round: org.aula.app.rpc.floor.v1.FloorRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.RaiseHandResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.RaiseHandResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.RaiseHandResponse> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.RaiseHandResponse by lazy { org.aula.app.rpc.floor.v1.RaiseHandResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.RaiseHandResponse = org.aula.app.rpc.floor.v1.RaiseHandResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.RaiseHandResponse> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.RaiseHandResponse",
            messageClass = org.aula.app.rpc.floor.v1.RaiseHandResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.floor.v1.FloorRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.floor.v1.RaiseHandResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LowerHandRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.LowerHandRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.LowerHandRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.LowerHandRequest> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.LowerHandRequest by lazy { org.aula.app.rpc.floor.v1.LowerHandRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.LowerHandRequest = org.aula.app.rpc.floor.v1.LowerHandRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.LowerHandRequest> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.LowerHandRequest",
            messageClass = org.aula.app.rpc.floor.v1.LowerHandRequest::class,
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
                        value = org.aula.app.rpc.floor.v1.LowerHandRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LowerHandResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.LowerHandResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.LowerHandResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.LowerHandResponse> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.LowerHandResponse by lazy { org.aula.app.rpc.floor.v1.LowerHandResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.LowerHandResponse = org.aula.app.rpc.floor.v1.LowerHandResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.LowerHandResponse> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.LowerHandResponse",
            messageClass = org.aula.app.rpc.floor.v1.LowerHandResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class FinishMyTurnRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.FinishMyTurnRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.FinishMyTurnRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.FinishMyTurnRequest> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.FinishMyTurnRequest by lazy { org.aula.app.rpc.floor.v1.FinishMyTurnRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.FinishMyTurnRequest = org.aula.app.rpc.floor.v1.FinishMyTurnRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.FinishMyTurnRequest> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.FinishMyTurnRequest",
            messageClass = org.aula.app.rpc.floor.v1.FinishMyTurnRequest::class,
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
                        value = org.aula.app.rpc.floor.v1.FinishMyTurnRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class FinishMyTurnResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.FinishMyTurnResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.FinishMyTurnResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.FinishMyTurnResponse> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.FinishMyTurnResponse by lazy { org.aula.app.rpc.floor.v1.FinishMyTurnResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.FinishMyTurnResponse = org.aula.app.rpc.floor.v1.FinishMyTurnResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.FinishMyTurnResponse> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.FinishMyTurnResponse",
            messageClass = org.aula.app.rpc.floor.v1.FinishMyTurnResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class ResolveTurnRequest(
    val roundId: String = "",
    val verdict: org.aula.app.rpc.floor.v1.TurnVerdict = org.aula.app.rpc.floor.v1.TurnVerdict.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.ResolveTurnRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.ResolveTurnRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.ResolveTurnRequest> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.ResolveTurnRequest by lazy { org.aula.app.rpc.floor.v1.ResolveTurnRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.ResolveTurnRequest = org.aula.app.rpc.floor.v1.ResolveTurnRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.ResolveTurnRequest> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.ResolveTurnRequest",
            messageClass = org.aula.app.rpc.floor.v1.ResolveTurnRequest::class,
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
                        value = org.aula.app.rpc.floor.v1.ResolveTurnRequest::roundId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "verdict",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.floor.v1.TurnVerdict.Companion),
                        jsonName = "verdict",
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
                        value = org.aula.app.rpc.floor.v1.ResolveTurnRequest::verdict
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ResolveTurnResponse(
    val round: org.aula.app.rpc.floor.v1.FloorRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.ResolveTurnResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.ResolveTurnResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.ResolveTurnResponse> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.ResolveTurnResponse by lazy { org.aula.app.rpc.floor.v1.ResolveTurnResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.ResolveTurnResponse = org.aula.app.rpc.floor.v1.ResolveTurnResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.ResolveTurnResponse> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.ResolveTurnResponse",
            messageClass = org.aula.app.rpc.floor.v1.ResolveTurnResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.floor.v1.FloorRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.floor.v1.ResolveTurnResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenFloorToAllRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.OpenFloorToAllRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.OpenFloorToAllRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.OpenFloorToAllRequest> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.OpenFloorToAllRequest by lazy { org.aula.app.rpc.floor.v1.OpenFloorToAllRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.OpenFloorToAllRequest = org.aula.app.rpc.floor.v1.OpenFloorToAllRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.OpenFloorToAllRequest> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.OpenFloorToAllRequest",
            messageClass = org.aula.app.rpc.floor.v1.OpenFloorToAllRequest::class,
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
                        value = org.aula.app.rpc.floor.v1.OpenFloorToAllRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenFloorToAllResponse(
    val round: org.aula.app.rpc.floor.v1.FloorRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.OpenFloorToAllResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.OpenFloorToAllResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.OpenFloorToAllResponse> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.OpenFloorToAllResponse by lazy { org.aula.app.rpc.floor.v1.OpenFloorToAllResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.OpenFloorToAllResponse = org.aula.app.rpc.floor.v1.OpenFloorToAllResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.OpenFloorToAllResponse> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.OpenFloorToAllResponse",
            messageClass = org.aula.app.rpc.floor.v1.OpenFloorToAllResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.floor.v1.FloorRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.floor.v1.OpenFloorToAllResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseFloorRoundRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.CloseFloorRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.CloseFloorRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.CloseFloorRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.CloseFloorRoundRequest by lazy { org.aula.app.rpc.floor.v1.CloseFloorRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.CloseFloorRoundRequest = org.aula.app.rpc.floor.v1.CloseFloorRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.CloseFloorRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.CloseFloorRoundRequest",
            messageClass = org.aula.app.rpc.floor.v1.CloseFloorRoundRequest::class,
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
                        value = org.aula.app.rpc.floor.v1.CloseFloorRoundRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseFloorRoundResponse(
    val round: org.aula.app.rpc.floor.v1.FloorRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.CloseFloorRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.CloseFloorRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.CloseFloorRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.CloseFloorRoundResponse by lazy { org.aula.app.rpc.floor.v1.CloseFloorRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.CloseFloorRoundResponse = org.aula.app.rpc.floor.v1.CloseFloorRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.CloseFloorRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.CloseFloorRoundResponse",
            messageClass = org.aula.app.rpc.floor.v1.CloseFloorRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.floor.v1.FloorRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.floor.v1.CloseFloorRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetFloorRoundRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.GetFloorRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.GetFloorRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.GetFloorRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.GetFloorRoundRequest by lazy { org.aula.app.rpc.floor.v1.GetFloorRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.GetFloorRoundRequest = org.aula.app.rpc.floor.v1.GetFloorRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.GetFloorRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.GetFloorRoundRequest",
            messageClass = org.aula.app.rpc.floor.v1.GetFloorRoundRequest::class,
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
                        value = org.aula.app.rpc.floor.v1.GetFloorRoundRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetFloorRoundResponse(
    val round: org.aula.app.rpc.floor.v1.FloorRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.floor.v1.GetFloorRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.GetFloorRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.floor.v1.GetFloorRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.floor.v1.GetFloorRoundResponse by lazy { org.aula.app.rpc.floor.v1.GetFloorRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.floor.v1.GetFloorRoundResponse = org.aula.app.rpc.floor.v1.GetFloorRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.floor.v1.GetFloorRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.floor.v1.GetFloorRoundResponse",
            messageClass = org.aula.app.rpc.floor.v1.GetFloorRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.floor.v1.FloorRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.floor.v1.GetFloorRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForResponder")
public fun Responder?.orDefault(): org.aula.app.rpc.floor.v1.Responder = this ?: Responder.defaultInstance

private fun Responder.protoMergeImpl(plus: pbandk.Message?): Responder = (plus as? Responder)?.let {
    it.copy(
        deadlineAt = deadlineAt?.plus(plus.deadlineAt) ?: plus.deadlineAt,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Responder.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Responder {
    var studentId = ""
    var name = ""
    var deadlineAt: pbandk.wkt.Timestamp? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> studentId = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> deadlineAt = _fieldValue as pbandk.wkt.Timestamp
        }
    }

    return Responder(studentId, name, deadlineAt, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForQueueEntry")
public fun QueueEntry?.orDefault(): org.aula.app.rpc.floor.v1.QueueEntry = this ?: QueueEntry.defaultInstance

private fun QueueEntry.protoMergeImpl(plus: pbandk.Message?): QueueEntry = (plus as? QueueEntry)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun QueueEntry.Companion.decodeWithImpl(u: pbandk.MessageDecoder): QueueEntry {
    var studentId = ""
    var name = ""
    var offsetMs = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> studentId = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> offsetMs = _fieldValue as Int
        }
    }

    return QueueEntry(studentId, name, offsetMs, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForFloorRound")
public fun FloorRound?.orDefault(): org.aula.app.rpc.floor.v1.FloorRound = this ?: FloorRound.defaultInstance

private fun FloorRound.protoMergeImpl(plus: pbandk.Message?): FloorRound = (plus as? FloorRound)?.let {
    it.copy(
        current = current?.plus(plus.current) ?: plus.current,
        queue = queue + plus.queue,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun FloorRound.Companion.decodeWithImpl(u: pbandk.MessageDecoder): FloorRound {
    var id = ""
    var lessonId = ""
    var prompt = ""
    var status: org.aula.app.rpc.floor.v1.FloorRoundStatus = org.aula.app.rpc.floor.v1.FloorRoundStatus.fromValue(0)
    var openToAll = false
    var current: org.aula.app.rpc.floor.v1.Responder? = null
    var queue: pbandk.ListWithSize.Builder<org.aula.app.rpc.floor.v1.QueueEntry>? = null
    var myState: org.aula.app.rpc.floor.v1.FloorTurnState = org.aula.app.rpc.floor.v1.FloorTurnState.fromValue(0)
    var myPosition = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> lessonId = _fieldValue as String
            3 -> prompt = _fieldValue as String
            4 -> status = _fieldValue as org.aula.app.rpc.floor.v1.FloorRoundStatus
            5 -> openToAll = _fieldValue as Boolean
            6 -> current = _fieldValue as org.aula.app.rpc.floor.v1.Responder
            7 -> queue = (queue ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.floor.v1.QueueEntry> }
            8 -> myState = _fieldValue as org.aula.app.rpc.floor.v1.FloorTurnState
            9 -> myPosition = _fieldValue as Int
        }
    }

    return FloorRound(id, lessonId, prompt, status,
        openToAll, current, pbandk.ListWithSize.Builder.fixed(queue), myState,
        myPosition, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenFloorRoundRequest")
public fun OpenFloorRoundRequest?.orDefault(): org.aula.app.rpc.floor.v1.OpenFloorRoundRequest = this ?: OpenFloorRoundRequest.defaultInstance

private fun OpenFloorRoundRequest.protoMergeImpl(plus: pbandk.Message?): OpenFloorRoundRequest = (plus as? OpenFloorRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenFloorRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenFloorRoundRequest {
    var lessonId = ""
    var prompt = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
            2 -> prompt = _fieldValue as String
        }
    }

    return OpenFloorRoundRequest(lessonId, prompt, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenFloorRoundResponse")
public fun OpenFloorRoundResponse?.orDefault(): org.aula.app.rpc.floor.v1.OpenFloorRoundResponse = this ?: OpenFloorRoundResponse.defaultInstance

private fun OpenFloorRoundResponse.protoMergeImpl(plus: pbandk.Message?): OpenFloorRoundResponse = (plus as? OpenFloorRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenFloorRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenFloorRoundResponse {
    var round: org.aula.app.rpc.floor.v1.FloorRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.floor.v1.FloorRound
        }
    }

    return OpenFloorRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRaiseHandRequest")
public fun RaiseHandRequest?.orDefault(): org.aula.app.rpc.floor.v1.RaiseHandRequest = this ?: RaiseHandRequest.defaultInstance

private fun RaiseHandRequest.protoMergeImpl(plus: pbandk.Message?): RaiseHandRequest = (plus as? RaiseHandRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RaiseHandRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RaiseHandRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return RaiseHandRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRaiseHandResponse")
public fun RaiseHandResponse?.orDefault(): org.aula.app.rpc.floor.v1.RaiseHandResponse = this ?: RaiseHandResponse.defaultInstance

private fun RaiseHandResponse.protoMergeImpl(plus: pbandk.Message?): RaiseHandResponse = (plus as? RaiseHandResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RaiseHandResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RaiseHandResponse {
    var round: org.aula.app.rpc.floor.v1.FloorRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.floor.v1.FloorRound
        }
    }

    return RaiseHandResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLowerHandRequest")
public fun LowerHandRequest?.orDefault(): org.aula.app.rpc.floor.v1.LowerHandRequest = this ?: LowerHandRequest.defaultInstance

private fun LowerHandRequest.protoMergeImpl(plus: pbandk.Message?): LowerHandRequest = (plus as? LowerHandRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LowerHandRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LowerHandRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return LowerHandRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLowerHandResponse")
public fun LowerHandResponse?.orDefault(): org.aula.app.rpc.floor.v1.LowerHandResponse = this ?: LowerHandResponse.defaultInstance

private fun LowerHandResponse.protoMergeImpl(plus: pbandk.Message?): LowerHandResponse = (plus as? LowerHandResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LowerHandResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LowerHandResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return LowerHandResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForFinishMyTurnRequest")
public fun FinishMyTurnRequest?.orDefault(): org.aula.app.rpc.floor.v1.FinishMyTurnRequest = this ?: FinishMyTurnRequest.defaultInstance

private fun FinishMyTurnRequest.protoMergeImpl(plus: pbandk.Message?): FinishMyTurnRequest = (plus as? FinishMyTurnRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun FinishMyTurnRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): FinishMyTurnRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return FinishMyTurnRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForFinishMyTurnResponse")
public fun FinishMyTurnResponse?.orDefault(): org.aula.app.rpc.floor.v1.FinishMyTurnResponse = this ?: FinishMyTurnResponse.defaultInstance

private fun FinishMyTurnResponse.protoMergeImpl(plus: pbandk.Message?): FinishMyTurnResponse = (plus as? FinishMyTurnResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun FinishMyTurnResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): FinishMyTurnResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return FinishMyTurnResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForResolveTurnRequest")
public fun ResolveTurnRequest?.orDefault(): org.aula.app.rpc.floor.v1.ResolveTurnRequest = this ?: ResolveTurnRequest.defaultInstance

private fun ResolveTurnRequest.protoMergeImpl(plus: pbandk.Message?): ResolveTurnRequest = (plus as? ResolveTurnRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ResolveTurnRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ResolveTurnRequest {
    var roundId = ""
    var verdict: org.aula.app.rpc.floor.v1.TurnVerdict = org.aula.app.rpc.floor.v1.TurnVerdict.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
            2 -> verdict = _fieldValue as org.aula.app.rpc.floor.v1.TurnVerdict
        }
    }

    return ResolveTurnRequest(roundId, verdict, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForResolveTurnResponse")
public fun ResolveTurnResponse?.orDefault(): org.aula.app.rpc.floor.v1.ResolveTurnResponse = this ?: ResolveTurnResponse.defaultInstance

private fun ResolveTurnResponse.protoMergeImpl(plus: pbandk.Message?): ResolveTurnResponse = (plus as? ResolveTurnResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ResolveTurnResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ResolveTurnResponse {
    var round: org.aula.app.rpc.floor.v1.FloorRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.floor.v1.FloorRound
        }
    }

    return ResolveTurnResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenFloorToAllRequest")
public fun OpenFloorToAllRequest?.orDefault(): org.aula.app.rpc.floor.v1.OpenFloorToAllRequest = this ?: OpenFloorToAllRequest.defaultInstance

private fun OpenFloorToAllRequest.protoMergeImpl(plus: pbandk.Message?): OpenFloorToAllRequest = (plus as? OpenFloorToAllRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenFloorToAllRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenFloorToAllRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return OpenFloorToAllRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenFloorToAllResponse")
public fun OpenFloorToAllResponse?.orDefault(): org.aula.app.rpc.floor.v1.OpenFloorToAllResponse = this ?: OpenFloorToAllResponse.defaultInstance

private fun OpenFloorToAllResponse.protoMergeImpl(plus: pbandk.Message?): OpenFloorToAllResponse = (plus as? OpenFloorToAllResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenFloorToAllResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenFloorToAllResponse {
    var round: org.aula.app.rpc.floor.v1.FloorRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.floor.v1.FloorRound
        }
    }

    return OpenFloorToAllResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseFloorRoundRequest")
public fun CloseFloorRoundRequest?.orDefault(): org.aula.app.rpc.floor.v1.CloseFloorRoundRequest = this ?: CloseFloorRoundRequest.defaultInstance

private fun CloseFloorRoundRequest.protoMergeImpl(plus: pbandk.Message?): CloseFloorRoundRequest = (plus as? CloseFloorRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseFloorRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseFloorRoundRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return CloseFloorRoundRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseFloorRoundResponse")
public fun CloseFloorRoundResponse?.orDefault(): org.aula.app.rpc.floor.v1.CloseFloorRoundResponse = this ?: CloseFloorRoundResponse.defaultInstance

private fun CloseFloorRoundResponse.protoMergeImpl(plus: pbandk.Message?): CloseFloorRoundResponse = (plus as? CloseFloorRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseFloorRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseFloorRoundResponse {
    var round: org.aula.app.rpc.floor.v1.FloorRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.floor.v1.FloorRound
        }
    }

    return CloseFloorRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetFloorRoundRequest")
public fun GetFloorRoundRequest?.orDefault(): org.aula.app.rpc.floor.v1.GetFloorRoundRequest = this ?: GetFloorRoundRequest.defaultInstance

private fun GetFloorRoundRequest.protoMergeImpl(plus: pbandk.Message?): GetFloorRoundRequest = (plus as? GetFloorRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetFloorRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetFloorRoundRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return GetFloorRoundRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetFloorRoundResponse")
public fun GetFloorRoundResponse?.orDefault(): org.aula.app.rpc.floor.v1.GetFloorRoundResponse = this ?: GetFloorRoundResponse.defaultInstance

private fun GetFloorRoundResponse.protoMergeImpl(plus: pbandk.Message?): GetFloorRoundResponse = (plus as? GetFloorRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetFloorRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetFloorRoundResponse {
    var round: org.aula.app.rpc.floor.v1.FloorRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.floor.v1.FloorRound
        }
    }

    return GetFloorRoundResponse(round, unknownFields)
}
