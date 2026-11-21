@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.attendance.v1

@pbandk.Export
public sealed class AttendanceRoundStatus(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.attendance.v1.AttendanceRoundStatus && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "AttendanceRoundStatus.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : AttendanceRoundStatus(0, "ATTENDANCE_ROUND_STATUS_UNSPECIFIED")
    public object OPEN : AttendanceRoundStatus(1, "ATTENDANCE_ROUND_STATUS_OPEN")
    public object CLOSED : AttendanceRoundStatus(2, "ATTENDANCE_ROUND_STATUS_CLOSED")
    public class UNRECOGNIZED(value: Int) : AttendanceRoundStatus(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.attendance.v1.AttendanceRoundStatus> {
        public val values: List<org.aula.app.rpc.attendance.v1.AttendanceRoundStatus> by lazy { listOf(UNSPECIFIED, OPEN, CLOSED) }
        override fun fromValue(value: Int): org.aula.app.rpc.attendance.v1.AttendanceRoundStatus = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.attendance.v1.AttendanceRoundStatus = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No AttendanceRoundStatus with name: $name")
    }
}

@pbandk.Export
public sealed class AttendanceStatus(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.attendance.v1.AttendanceStatus && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "AttendanceStatus.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : AttendanceStatus(0, "ATTENDANCE_STATUS_UNSPECIFIED")
    public object PRESENT : AttendanceStatus(1, "ATTENDANCE_STATUS_PRESENT")
    public object NEEDS_REVIEW : AttendanceStatus(2, "ATTENDANCE_STATUS_NEEDS_REVIEW")
    public object ABSENT : AttendanceStatus(3, "ATTENDANCE_STATUS_ABSENT")
    public class UNRECOGNIZED(value: Int) : AttendanceStatus(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.attendance.v1.AttendanceStatus> {
        public val values: List<org.aula.app.rpc.attendance.v1.AttendanceStatus> by lazy { listOf(UNSPECIFIED, PRESENT, NEEDS_REVIEW, ABSENT) }
        override fun fromValue(value: Int): org.aula.app.rpc.attendance.v1.AttendanceStatus = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.attendance.v1.AttendanceStatus = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No AttendanceStatus with name: $name")
    }
}

@pbandk.Export
public sealed class ReviewReason(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.attendance.v1.ReviewReason && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "ReviewReason.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : ReviewReason(0, "REVIEW_REASON_UNSPECIFIED")
    public object TOO_FAR : ReviewReason(1, "REVIEW_REASON_TOO_FAR")
    public object NO_LOCATION : ReviewReason(2, "REVIEW_REASON_NO_LOCATION")
    public object LOW_ACCURACY : ReviewReason(3, "REVIEW_REASON_LOW_ACCURACY")
    public class UNRECOGNIZED(value: Int) : ReviewReason(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.attendance.v1.ReviewReason> {
        public val values: List<org.aula.app.rpc.attendance.v1.ReviewReason> by lazy { listOf(UNSPECIFIED, TOO_FAR, NO_LOCATION, LOW_ACCURACY) }
        override fun fromValue(value: Int): org.aula.app.rpc.attendance.v1.ReviewReason = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.attendance.v1.ReviewReason = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No ReviewReason with name: $name")
    }
}

@pbandk.Export
public sealed class Decision(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.attendance.v1.Decision && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "Decision.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : Decision(0, "DECISION_UNSPECIFIED")
    public object ACCEPT : Decision(1, "DECISION_ACCEPT")
    public object REJECT : Decision(2, "DECISION_REJECT")
    public class UNRECOGNIZED(value: Int) : Decision(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.attendance.v1.Decision> {
        public val values: List<org.aula.app.rpc.attendance.v1.Decision> by lazy { listOf(UNSPECIFIED, ACCEPT, REJECT) }
        override fun fromValue(value: Int): org.aula.app.rpc.attendance.v1.Decision = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.attendance.v1.Decision = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No Decision with name: $name")
    }
}

@pbandk.Export
public data class ReviewItem(
    val studentId: String = "",
    val name: String = "",
    val reason: org.aula.app.rpc.attendance.v1.ReviewReason = org.aula.app.rpc.attendance.v1.ReviewReason.fromValue(0),
    val distanceM: Int = 0,
    val status: org.aula.app.rpc.attendance.v1.AttendanceStatus = org.aula.app.rpc.attendance.v1.AttendanceStatus.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.ReviewItem = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.ReviewItem> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.ReviewItem> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.ReviewItem by lazy { org.aula.app.rpc.attendance.v1.ReviewItem() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.ReviewItem = org.aula.app.rpc.attendance.v1.ReviewItem.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.ReviewItem> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.ReviewItem",
            messageClass = org.aula.app.rpc.attendance.v1.ReviewItem::class,
            messageCompanion = this,
            fields = buildList(5) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "studentId",
                        value = org.aula.app.rpc.attendance.v1.ReviewItem::studentId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.attendance.v1.ReviewItem::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "reason",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.attendance.v1.ReviewReason.Companion),
                        jsonName = "reason",
                        value = org.aula.app.rpc.attendance.v1.ReviewItem::reason
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "distance_m",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "distanceM",
                        value = org.aula.app.rpc.attendance.v1.ReviewItem::distanceM
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.attendance.v1.AttendanceStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.attendance.v1.ReviewItem::status
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class AttendanceRound(
    val id: String = "",
    val lessonId: String = "",
    val status: org.aula.app.rpc.attendance.v1.AttendanceRoundStatus = org.aula.app.rpc.attendance.v1.AttendanceRoundStatus.fromValue(0),
    val presentCount: Int = 0,
    val enrolledCount: Int = 0,
    val inRoomCount: Int = 0,
    val review: List<org.aula.app.rpc.attendance.v1.ReviewItem> = emptyList(),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.AttendanceRound = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.AttendanceRound> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.AttendanceRound> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.AttendanceRound by lazy { org.aula.app.rpc.attendance.v1.AttendanceRound() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.AttendanceRound = org.aula.app.rpc.attendance.v1.AttendanceRound.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.AttendanceRound> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.AttendanceRound",
            messageClass = org.aula.app.rpc.attendance.v1.AttendanceRound::class,
            messageCompanion = this,
            fields = buildList(7) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.attendance.v1.AttendanceRound::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lesson_id",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "lessonId",
                        value = org.aula.app.rpc.attendance.v1.AttendanceRound::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.attendance.v1.AttendanceRoundStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.attendance.v1.AttendanceRound::status
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "present_count",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "presentCount",
                        value = org.aula.app.rpc.attendance.v1.AttendanceRound::presentCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "enrolled_count",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "enrolledCount",
                        value = org.aula.app.rpc.attendance.v1.AttendanceRound::enrolledCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "in_room_count",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "inRoomCount",
                        value = org.aula.app.rpc.attendance.v1.AttendanceRound::inRoomCount
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "review",
                        number = 7,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.attendance.v1.ReviewItem>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.attendance.v1.ReviewItem.Companion)),
                        jsonName = "review",
                        value = org.aula.app.rpc.attendance.v1.AttendanceRound::review
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenAttendanceRoundRequest(
    val lessonId: String = "",
    val hasLocation: Boolean = false,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest by lazy { org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.OpenAttendanceRoundRequest",
            messageClass = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest::class,
            messageCompanion = this,
            fields = buildList(4) {
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
                        value = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest::lessonId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "has_location",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "hasLocation",
                        value = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest::hasLocation
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lat",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Double(),
                        jsonName = "lat",
                        value = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest::lat
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lng",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Double(),
                        jsonName = "lng",
                        value = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest::lng
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class OpenAttendanceRoundResponse(
    val round: org.aula.app.rpc.attendance.v1.AttendanceRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse by lazy { org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.OpenAttendanceRoundResponse",
            messageClass = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.attendance.v1.AttendanceRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetAttendanceCodesRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest by lazy { org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest = org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.GetAttendanceCodesRequest",
            messageClass = org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest::class,
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
                        value = org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetAttendanceCodesResponse(
    val qrCode: String = "",
    val qrExpiresAt: pbandk.wkt.Timestamp? = null,
    val manualCode: String = "",
    val manualExpiresAt: pbandk.wkt.Timestamp? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse by lazy { org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse = org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.GetAttendanceCodesResponse",
            messageClass = org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "qr_code",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "qrCode",
                        value = org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse::qrCode
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "qr_expires_at",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "qrExpiresAt",
                        value = org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse::qrExpiresAt
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "manual_code",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "manualCode",
                        value = org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse::manualCode
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "manual_expires_at",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = pbandk.wkt.Timestamp.Companion),
                        jsonName = "manualExpiresAt",
                        value = org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse::manualExpiresAt
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetAttendanceRoundRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest by lazy { org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest = org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.GetAttendanceRoundRequest",
            messageClass = org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest::class,
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
                        value = org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetAttendanceRoundResponse(
    val round: org.aula.app.rpc.attendance.v1.AttendanceRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse by lazy { org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse = org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.GetAttendanceRoundResponse",
            messageClass = org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.attendance.v1.AttendanceRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CheckInRequest(
    val roundId: String = "",
    val code: String = "",
    val hasLocation: Boolean = false,
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val accuracyM: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.CheckInRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.CheckInRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.CheckInRequest> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.CheckInRequest by lazy { org.aula.app.rpc.attendance.v1.CheckInRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.CheckInRequest = org.aula.app.rpc.attendance.v1.CheckInRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.CheckInRequest> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.CheckInRequest",
            messageClass = org.aula.app.rpc.attendance.v1.CheckInRequest::class,
            messageCompanion = this,
            fields = buildList(6) {
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
                        value = org.aula.app.rpc.attendance.v1.CheckInRequest::roundId
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
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(6, 114, 4, 16, 1, 24, 64))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.attendance.v1.CheckInRequest::code
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "has_location",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "hasLocation",
                        value = org.aula.app.rpc.attendance.v1.CheckInRequest::hasLocation
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lat",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Double(),
                        jsonName = "lat",
                        value = org.aula.app.rpc.attendance.v1.CheckInRequest::lat
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "lng",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Double(),
                        jsonName = "lng",
                        value = org.aula.app.rpc.attendance.v1.CheckInRequest::lng
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "accuracy_m",
                        number = 6,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "accuracyM",
                        value = org.aula.app.rpc.attendance.v1.CheckInRequest::accuracyM
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CheckInResponse(
    val status: org.aula.app.rpc.attendance.v1.AttendanceStatus = org.aula.app.rpc.attendance.v1.AttendanceStatus.fromValue(0),
    val reason: org.aula.app.rpc.attendance.v1.ReviewReason = org.aula.app.rpc.attendance.v1.ReviewReason.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.CheckInResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.CheckInResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.CheckInResponse> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.CheckInResponse by lazy { org.aula.app.rpc.attendance.v1.CheckInResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.CheckInResponse = org.aula.app.rpc.attendance.v1.CheckInResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.CheckInResponse> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.CheckInResponse",
            messageClass = org.aula.app.rpc.attendance.v1.CheckInResponse::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "status",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.attendance.v1.AttendanceStatus.Companion),
                        jsonName = "status",
                        value = org.aula.app.rpc.attendance.v1.CheckInResponse::status
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "reason",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.attendance.v1.ReviewReason.Companion),
                        jsonName = "reason",
                        value = org.aula.app.rpc.attendance.v1.CheckInResponse::reason
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseAttendanceRoundRequest(
    val roundId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest by lazy { org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest = org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.CloseAttendanceRoundRequest",
            messageClass = org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest::class,
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
                        value = org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest::roundId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CloseAttendanceRoundResponse(
    val round: org.aula.app.rpc.attendance.v1.AttendanceRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse by lazy { org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse = org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.CloseAttendanceRoundResponse",
            messageClass = org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.attendance.v1.AttendanceRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ResolveAttendanceRequest(
    val roundId: String = "",
    val studentId: String = "",
    val decision: org.aula.app.rpc.attendance.v1.Decision = org.aula.app.rpc.attendance.v1.Decision.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest by lazy { org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest = org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.ResolveAttendanceRequest",
            messageClass = org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest::class,
            messageCompanion = this,
            fields = buildList(3) {
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
                        value = org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest::roundId
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
                        value = org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest::studentId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "decision",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.attendance.v1.Decision.Companion),
                        jsonName = "decision",
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
                        value = org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest::decision
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ResolveAttendanceResponse(
    val round: org.aula.app.rpc.attendance.v1.AttendanceRound? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse> {
        public val defaultInstance: org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse by lazy { org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse = org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse> = pbandk.MessageDescriptor(
            fullName = "aula.attendance.v1.ResolveAttendanceResponse",
            messageClass = org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "round",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.attendance.v1.AttendanceRound.Companion),
                        jsonName = "round",
                        value = org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse::round
                    )
                )
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForReviewItem")
public fun ReviewItem?.orDefault(): org.aula.app.rpc.attendance.v1.ReviewItem = this ?: ReviewItem.defaultInstance

private fun ReviewItem.protoMergeImpl(plus: pbandk.Message?): ReviewItem = (plus as? ReviewItem)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ReviewItem.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ReviewItem {
    var studentId = ""
    var name = ""
    var reason: org.aula.app.rpc.attendance.v1.ReviewReason = org.aula.app.rpc.attendance.v1.ReviewReason.fromValue(0)
    var distanceM = 0
    var status: org.aula.app.rpc.attendance.v1.AttendanceStatus = org.aula.app.rpc.attendance.v1.AttendanceStatus.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> studentId = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> reason = _fieldValue as org.aula.app.rpc.attendance.v1.ReviewReason
            4 -> distanceM = _fieldValue as Int
            5 -> status = _fieldValue as org.aula.app.rpc.attendance.v1.AttendanceStatus
        }
    }

    return ReviewItem(studentId, name, reason, distanceM,
        status, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForAttendanceRound")
public fun AttendanceRound?.orDefault(): org.aula.app.rpc.attendance.v1.AttendanceRound = this ?: AttendanceRound.defaultInstance

private fun AttendanceRound.protoMergeImpl(plus: pbandk.Message?): AttendanceRound = (plus as? AttendanceRound)?.let {
    it.copy(
        review = review + plus.review,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun AttendanceRound.Companion.decodeWithImpl(u: pbandk.MessageDecoder): AttendanceRound {
    var id = ""
    var lessonId = ""
    var status: org.aula.app.rpc.attendance.v1.AttendanceRoundStatus = org.aula.app.rpc.attendance.v1.AttendanceRoundStatus.fromValue(0)
    var presentCount = 0
    var enrolledCount = 0
    var inRoomCount = 0
    var review: pbandk.ListWithSize.Builder<org.aula.app.rpc.attendance.v1.ReviewItem>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> lessonId = _fieldValue as String
            3 -> status = _fieldValue as org.aula.app.rpc.attendance.v1.AttendanceRoundStatus
            4 -> presentCount = _fieldValue as Int
            5 -> enrolledCount = _fieldValue as Int
            6 -> inRoomCount = _fieldValue as Int
            7 -> review = (review ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.attendance.v1.ReviewItem> }
        }
    }

    return AttendanceRound(id, lessonId, status, presentCount,
        enrolledCount, inRoomCount, pbandk.ListWithSize.Builder.fixed(review), unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenAttendanceRoundRequest")
public fun OpenAttendanceRoundRequest?.orDefault(): org.aula.app.rpc.attendance.v1.OpenAttendanceRoundRequest = this ?: OpenAttendanceRoundRequest.defaultInstance

private fun OpenAttendanceRoundRequest.protoMergeImpl(plus: pbandk.Message?): OpenAttendanceRoundRequest = (plus as? OpenAttendanceRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenAttendanceRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenAttendanceRoundRequest {
    var lessonId = ""
    var hasLocation = false
    var lat = 0.0
    var lng = 0.0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> lessonId = _fieldValue as String
            2 -> hasLocation = _fieldValue as Boolean
            3 -> lat = _fieldValue as Double
            4 -> lng = _fieldValue as Double
        }
    }

    return OpenAttendanceRoundRequest(lessonId, hasLocation, lat, lng, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForOpenAttendanceRoundResponse")
public fun OpenAttendanceRoundResponse?.orDefault(): org.aula.app.rpc.attendance.v1.OpenAttendanceRoundResponse = this ?: OpenAttendanceRoundResponse.defaultInstance

private fun OpenAttendanceRoundResponse.protoMergeImpl(plus: pbandk.Message?): OpenAttendanceRoundResponse = (plus as? OpenAttendanceRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun OpenAttendanceRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): OpenAttendanceRoundResponse {
    var round: org.aula.app.rpc.attendance.v1.AttendanceRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.attendance.v1.AttendanceRound
        }
    }

    return OpenAttendanceRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetAttendanceCodesRequest")
public fun GetAttendanceCodesRequest?.orDefault(): org.aula.app.rpc.attendance.v1.GetAttendanceCodesRequest = this ?: GetAttendanceCodesRequest.defaultInstance

private fun GetAttendanceCodesRequest.protoMergeImpl(plus: pbandk.Message?): GetAttendanceCodesRequest = (plus as? GetAttendanceCodesRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetAttendanceCodesRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetAttendanceCodesRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return GetAttendanceCodesRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetAttendanceCodesResponse")
public fun GetAttendanceCodesResponse?.orDefault(): org.aula.app.rpc.attendance.v1.GetAttendanceCodesResponse = this ?: GetAttendanceCodesResponse.defaultInstance

private fun GetAttendanceCodesResponse.protoMergeImpl(plus: pbandk.Message?): GetAttendanceCodesResponse = (plus as? GetAttendanceCodesResponse)?.let {
    it.copy(
        qrExpiresAt = qrExpiresAt?.plus(plus.qrExpiresAt) ?: plus.qrExpiresAt,
        manualExpiresAt = manualExpiresAt?.plus(plus.manualExpiresAt) ?: plus.manualExpiresAt,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetAttendanceCodesResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetAttendanceCodesResponse {
    var qrCode = ""
    var qrExpiresAt: pbandk.wkt.Timestamp? = null
    var manualCode = ""
    var manualExpiresAt: pbandk.wkt.Timestamp? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> qrCode = _fieldValue as String
            2 -> qrExpiresAt = _fieldValue as pbandk.wkt.Timestamp
            3 -> manualCode = _fieldValue as String
            4 -> manualExpiresAt = _fieldValue as pbandk.wkt.Timestamp
        }
    }

    return GetAttendanceCodesResponse(qrCode, qrExpiresAt, manualCode, manualExpiresAt, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetAttendanceRoundRequest")
public fun GetAttendanceRoundRequest?.orDefault(): org.aula.app.rpc.attendance.v1.GetAttendanceRoundRequest = this ?: GetAttendanceRoundRequest.defaultInstance

private fun GetAttendanceRoundRequest.protoMergeImpl(plus: pbandk.Message?): GetAttendanceRoundRequest = (plus as? GetAttendanceRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetAttendanceRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetAttendanceRoundRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return GetAttendanceRoundRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetAttendanceRoundResponse")
public fun GetAttendanceRoundResponse?.orDefault(): org.aula.app.rpc.attendance.v1.GetAttendanceRoundResponse = this ?: GetAttendanceRoundResponse.defaultInstance

private fun GetAttendanceRoundResponse.protoMergeImpl(plus: pbandk.Message?): GetAttendanceRoundResponse = (plus as? GetAttendanceRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetAttendanceRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetAttendanceRoundResponse {
    var round: org.aula.app.rpc.attendance.v1.AttendanceRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.attendance.v1.AttendanceRound
        }
    }

    return GetAttendanceRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCheckInRequest")
public fun CheckInRequest?.orDefault(): org.aula.app.rpc.attendance.v1.CheckInRequest = this ?: CheckInRequest.defaultInstance

private fun CheckInRequest.protoMergeImpl(plus: pbandk.Message?): CheckInRequest = (plus as? CheckInRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CheckInRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CheckInRequest {
    var roundId = ""
    var code = ""
    var hasLocation = false
    var lat = 0.0
    var lng = 0.0
    var accuracyM = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
            2 -> code = _fieldValue as String
            3 -> hasLocation = _fieldValue as Boolean
            4 -> lat = _fieldValue as Double
            5 -> lng = _fieldValue as Double
            6 -> accuracyM = _fieldValue as Int
        }
    }

    return CheckInRequest(roundId, code, hasLocation, lat,
        lng, accuracyM, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCheckInResponse")
public fun CheckInResponse?.orDefault(): org.aula.app.rpc.attendance.v1.CheckInResponse = this ?: CheckInResponse.defaultInstance

private fun CheckInResponse.protoMergeImpl(plus: pbandk.Message?): CheckInResponse = (plus as? CheckInResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CheckInResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CheckInResponse {
    var status: org.aula.app.rpc.attendance.v1.AttendanceStatus = org.aula.app.rpc.attendance.v1.AttendanceStatus.fromValue(0)
    var reason: org.aula.app.rpc.attendance.v1.ReviewReason = org.aula.app.rpc.attendance.v1.ReviewReason.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> status = _fieldValue as org.aula.app.rpc.attendance.v1.AttendanceStatus
            2 -> reason = _fieldValue as org.aula.app.rpc.attendance.v1.ReviewReason
        }
    }

    return CheckInResponse(status, reason, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseAttendanceRoundRequest")
public fun CloseAttendanceRoundRequest?.orDefault(): org.aula.app.rpc.attendance.v1.CloseAttendanceRoundRequest = this ?: CloseAttendanceRoundRequest.defaultInstance

private fun CloseAttendanceRoundRequest.protoMergeImpl(plus: pbandk.Message?): CloseAttendanceRoundRequest = (plus as? CloseAttendanceRoundRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseAttendanceRoundRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseAttendanceRoundRequest {
    var roundId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
        }
    }

    return CloseAttendanceRoundRequest(roundId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCloseAttendanceRoundResponse")
public fun CloseAttendanceRoundResponse?.orDefault(): org.aula.app.rpc.attendance.v1.CloseAttendanceRoundResponse = this ?: CloseAttendanceRoundResponse.defaultInstance

private fun CloseAttendanceRoundResponse.protoMergeImpl(plus: pbandk.Message?): CloseAttendanceRoundResponse = (plus as? CloseAttendanceRoundResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CloseAttendanceRoundResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CloseAttendanceRoundResponse {
    var round: org.aula.app.rpc.attendance.v1.AttendanceRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.attendance.v1.AttendanceRound
        }
    }

    return CloseAttendanceRoundResponse(round, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForResolveAttendanceRequest")
public fun ResolveAttendanceRequest?.orDefault(): org.aula.app.rpc.attendance.v1.ResolveAttendanceRequest = this ?: ResolveAttendanceRequest.defaultInstance

private fun ResolveAttendanceRequest.protoMergeImpl(plus: pbandk.Message?): ResolveAttendanceRequest = (plus as? ResolveAttendanceRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ResolveAttendanceRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ResolveAttendanceRequest {
    var roundId = ""
    var studentId = ""
    var decision: org.aula.app.rpc.attendance.v1.Decision = org.aula.app.rpc.attendance.v1.Decision.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> roundId = _fieldValue as String
            2 -> studentId = _fieldValue as String
            3 -> decision = _fieldValue as org.aula.app.rpc.attendance.v1.Decision
        }
    }

    return ResolveAttendanceRequest(roundId, studentId, decision, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForResolveAttendanceResponse")
public fun ResolveAttendanceResponse?.orDefault(): org.aula.app.rpc.attendance.v1.ResolveAttendanceResponse = this ?: ResolveAttendanceResponse.defaultInstance

private fun ResolveAttendanceResponse.protoMergeImpl(plus: pbandk.Message?): ResolveAttendanceResponse = (plus as? ResolveAttendanceResponse)?.let {
    it.copy(
        round = round?.plus(plus.round) ?: plus.round,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ResolveAttendanceResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ResolveAttendanceResponse {
    var round: org.aula.app.rpc.attendance.v1.AttendanceRound? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> round = _fieldValue as org.aula.app.rpc.attendance.v1.AttendanceRound
        }
    }

    return ResolveAttendanceResponse(round, unknownFields)
}
