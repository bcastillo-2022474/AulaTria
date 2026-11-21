@file:OptIn(pbandk.PublicForGeneratedCode::class)

package org.aula.app.rpc.identity.v1

@pbandk.Export
public sealed class Role(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.identity.v1.Role && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "Role.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : Role(0, "ROLE_UNSPECIFIED")
    public object ADMIN : Role(1, "ROLE_ADMIN")
    public object TEACHER : Role(2, "ROLE_TEACHER")
    public object STUDENT : Role(3, "ROLE_STUDENT")
    public class UNRECOGNIZED(value: Int) : Role(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.identity.v1.Role> {
        public val values: List<org.aula.app.rpc.identity.v1.Role> by lazy { listOf(UNSPECIFIED, ADMIN, TEACHER, STUDENT) }
        override fun fromValue(value: Int): org.aula.app.rpc.identity.v1.Role = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.identity.v1.Role = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No Role with name: $name")
    }
}

@pbandk.Export
public sealed class DevicePlatform(override val value: Int, override val name: String? = null) : pbandk.Message.Enum {
    override fun equals(other: kotlin.Any?): Boolean = other is org.aula.app.rpc.identity.v1.DevicePlatform && other.value == value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = "DevicePlatform.${name ?: "UNRECOGNIZED"}(value=$value)"

    public object UNSPECIFIED : DevicePlatform(0, "DEVICE_PLATFORM_UNSPECIFIED")
    public object IOS : DevicePlatform(1, "DEVICE_PLATFORM_IOS")
    public object ANDROID : DevicePlatform(2, "DEVICE_PLATFORM_ANDROID")
    public class UNRECOGNIZED(value: Int) : DevicePlatform(value)

    public companion object : pbandk.Message.Enum.Companion<org.aula.app.rpc.identity.v1.DevicePlatform> {
        public val values: List<org.aula.app.rpc.identity.v1.DevicePlatform> by lazy { listOf(UNSPECIFIED, IOS, ANDROID) }
        override fun fromValue(value: Int): org.aula.app.rpc.identity.v1.DevicePlatform = values.firstOrNull { it.value == value } ?: UNRECOGNIZED(value)
        override fun fromName(name: String): org.aula.app.rpc.identity.v1.DevicePlatform = values.firstOrNull { it.name == name } ?: throw IllegalArgumentException("No DevicePlatform with name: $name")
    }
}

@pbandk.Export
public data class School(
    val id: String = "",
    val name: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.School = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.School> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.School> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.School by lazy { org.aula.app.rpc.identity.v1.School() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.School = org.aula.app.rpc.identity.v1.School.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.School> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.School",
            messageClass = org.aula.app.rpc.identity.v1.School::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.identity.v1.School::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.identity.v1.School::name
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: org.aula.app.rpc.identity.v1.Role = org.aula.app.rpc.identity.v1.Role.fromValue(0),
    val school: org.aula.app.rpc.identity.v1.School? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.User = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.User> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.User> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.User by lazy { org.aula.app.rpc.identity.v1.User() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.User = org.aula.app.rpc.identity.v1.User.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.User> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.User",
            messageClass = org.aula.app.rpc.identity.v1.User::class,
            messageCompanion = this,
            fields = buildList(5) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.identity.v1.User::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.identity.v1.User::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "email",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "email",
                        value = org.aula.app.rpc.identity.v1.User::email
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "role",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.identity.v1.Role.Companion),
                        jsonName = "role",
                        value = org.aula.app.rpc.identity.v1.User::role
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "school",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.School.Companion),
                        jsonName = "school",
                        value = org.aula.app.rpc.identity.v1.User::school
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class Session(
    val accessToken: String = "",
    val refreshToken: String = "",
    val user: org.aula.app.rpc.identity.v1.User? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.Session = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.Session> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.Session> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.Session by lazy { org.aula.app.rpc.identity.v1.Session() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.Session = org.aula.app.rpc.identity.v1.Session.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.Session> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.Session",
            messageClass = org.aula.app.rpc.identity.v1.Session::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "access_token",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "accessToken",
                        value = org.aula.app.rpc.identity.v1.Session::accessToken
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "refresh_token",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "refreshToken",
                        value = org.aula.app.rpc.identity.v1.Session::refreshToken
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "user",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.User.Companion),
                        jsonName = "user",
                        value = org.aula.app.rpc.identity.v1.Session::user
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LoginRequest(
    val email: String = "",
    val password: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.LoginRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LoginRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.LoginRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.LoginRequest by lazy { org.aula.app.rpc.identity.v1.LoginRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.LoginRequest = org.aula.app.rpc.identity.v1.LoginRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LoginRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.LoginRequest",
            messageClass = org.aula.app.rpc.identity.v1.LoginRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "email",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "email",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 114, 5, 16, 3, 24, -2, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.identity.v1.LoginRequest::email
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "password",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "password",
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
                        value = org.aula.app.rpc.identity.v1.LoginRequest::password
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LoginResponse(
    val session: org.aula.app.rpc.identity.v1.Session? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.LoginResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LoginResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.LoginResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.LoginResponse by lazy { org.aula.app.rpc.identity.v1.LoginResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.LoginResponse = org.aula.app.rpc.identity.v1.LoginResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LoginResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.LoginResponse",
            messageClass = org.aula.app.rpc.identity.v1.LoginResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "session",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.Session.Companion),
                        jsonName = "session",
                        value = org.aula.app.rpc.identity.v1.LoginResponse::session
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RefreshSessionRequest(
    val refreshToken: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.RefreshSessionRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RefreshSessionRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.RefreshSessionRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.RefreshSessionRequest by lazy { org.aula.app.rpc.identity.v1.RefreshSessionRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.RefreshSessionRequest = org.aula.app.rpc.identity.v1.RefreshSessionRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RefreshSessionRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.RefreshSessionRequest",
            messageClass = org.aula.app.rpc.identity.v1.RefreshSessionRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "refresh_token",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "refreshToken",
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
                        value = org.aula.app.rpc.identity.v1.RefreshSessionRequest::refreshToken
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RefreshSessionResponse(
    val session: org.aula.app.rpc.identity.v1.Session? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.RefreshSessionResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RefreshSessionResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.RefreshSessionResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.RefreshSessionResponse by lazy { org.aula.app.rpc.identity.v1.RefreshSessionResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.RefreshSessionResponse = org.aula.app.rpc.identity.v1.RefreshSessionResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RefreshSessionResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.RefreshSessionResponse",
            messageClass = org.aula.app.rpc.identity.v1.RefreshSessionResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "session",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.Session.Companion),
                        jsonName = "session",
                        value = org.aula.app.rpc.identity.v1.RefreshSessionResponse::session
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LogoutRequest(
    val refreshToken: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.LogoutRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LogoutRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.LogoutRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.LogoutRequest by lazy { org.aula.app.rpc.identity.v1.LogoutRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.LogoutRequest = org.aula.app.rpc.identity.v1.LogoutRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LogoutRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.LogoutRequest",
            messageClass = org.aula.app.rpc.identity.v1.LogoutRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "refresh_token",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "refreshToken",
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
                        value = org.aula.app.rpc.identity.v1.LogoutRequest::refreshToken
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LogoutResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.LogoutResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LogoutResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.LogoutResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.LogoutResponse by lazy { org.aula.app.rpc.identity.v1.LogoutResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.LogoutResponse = org.aula.app.rpc.identity.v1.LogoutResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LogoutResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.LogoutResponse",
            messageClass = org.aula.app.rpc.identity.v1.LogoutResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class GetMeRequest(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.GetMeRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.GetMeRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.GetMeRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.GetMeRequest by lazy { org.aula.app.rpc.identity.v1.GetMeRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.GetMeRequest = org.aula.app.rpc.identity.v1.GetMeRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.GetMeRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.GetMeRequest",
            messageClass = org.aula.app.rpc.identity.v1.GetMeRequest::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class GetMeResponse(
    val user: org.aula.app.rpc.identity.v1.User? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.GetMeResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.GetMeResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.GetMeResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.GetMeResponse by lazy { org.aula.app.rpc.identity.v1.GetMeResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.GetMeResponse = org.aula.app.rpc.identity.v1.GetMeResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.GetMeResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.GetMeResponse",
            messageClass = org.aula.app.rpc.identity.v1.GetMeResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "user",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.User.Companion),
                        jsonName = "user",
                        value = org.aula.app.rpc.identity.v1.GetMeResponse::user
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetInvitationRequest(
    val token: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.GetInvitationRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.GetInvitationRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.GetInvitationRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.GetInvitationRequest by lazy { org.aula.app.rpc.identity.v1.GetInvitationRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.GetInvitationRequest = org.aula.app.rpc.identity.v1.GetInvitationRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.GetInvitationRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.GetInvitationRequest",
            messageClass = org.aula.app.rpc.identity.v1.GetInvitationRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "token",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "token",
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
                        value = org.aula.app.rpc.identity.v1.GetInvitationRequest::token
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class GetInvitationResponse(
    val schoolName: String = "",
    val name: String = "",
    val email: String = "",
    val role: org.aula.app.rpc.identity.v1.Role = org.aula.app.rpc.identity.v1.Role.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.GetInvitationResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.GetInvitationResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.GetInvitationResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.GetInvitationResponse by lazy { org.aula.app.rpc.identity.v1.GetInvitationResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.GetInvitationResponse = org.aula.app.rpc.identity.v1.GetInvitationResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.GetInvitationResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.GetInvitationResponse",
            messageClass = org.aula.app.rpc.identity.v1.GetInvitationResponse::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "school_name",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "schoolName",
                        value = org.aula.app.rpc.identity.v1.GetInvitationResponse::schoolName
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.identity.v1.GetInvitationResponse::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "email",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "email",
                        value = org.aula.app.rpc.identity.v1.GetInvitationResponse::email
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "role",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.identity.v1.Role.Companion),
                        jsonName = "role",
                        value = org.aula.app.rpc.identity.v1.GetInvitationResponse::role
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class AcceptInvitationRequest(
    val token: String = "",
    val password: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.AcceptInvitationRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.AcceptInvitationRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.AcceptInvitationRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.AcceptInvitationRequest by lazy { org.aula.app.rpc.identity.v1.AcceptInvitationRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.AcceptInvitationRequest = org.aula.app.rpc.identity.v1.AcceptInvitationRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.AcceptInvitationRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.AcceptInvitationRequest",
            messageClass = org.aula.app.rpc.identity.v1.AcceptInvitationRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "token",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "token",
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
                        value = org.aula.app.rpc.identity.v1.AcceptInvitationRequest::token
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "password",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "password",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 114, 5, 16, 8, 24, -56, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.identity.v1.AcceptInvitationRequest::password
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class AcceptInvitationResponse(
    val session: org.aula.app.rpc.identity.v1.Session? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.AcceptInvitationResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.AcceptInvitationResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.AcceptInvitationResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.AcceptInvitationResponse by lazy { org.aula.app.rpc.identity.v1.AcceptInvitationResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.AcceptInvitationResponse = org.aula.app.rpc.identity.v1.AcceptInvitationResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.AcceptInvitationResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.AcceptInvitationResponse",
            messageClass = org.aula.app.rpc.identity.v1.AcceptInvitationResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "session",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.Session.Companion),
                        jsonName = "session",
                        value = org.aula.app.rpc.identity.v1.AcceptInvitationResponse::session
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LookupJoinCodeRequest(
    val joinCode: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.LookupJoinCodeRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LookupJoinCodeRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.LookupJoinCodeRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.LookupJoinCodeRequest by lazy { org.aula.app.rpc.identity.v1.LookupJoinCodeRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.LookupJoinCodeRequest = org.aula.app.rpc.identity.v1.LookupJoinCodeRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LookupJoinCodeRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.LookupJoinCodeRequest",
            messageClass = org.aula.app.rpc.identity.v1.LookupJoinCodeRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "join_code",
                        number = 1,
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
                        value = org.aula.app.rpc.identity.v1.LookupJoinCodeRequest::joinCode
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class LookupJoinCodeResponse(
    val courseId: String = "",
    val courseName: String = "",
    val courseCode: String = "",
    val schoolName: String = "",
    val studentCount: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.LookupJoinCodeResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LookupJoinCodeResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.LookupJoinCodeResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.LookupJoinCodeResponse by lazy { org.aula.app.rpc.identity.v1.LookupJoinCodeResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.LookupJoinCodeResponse = org.aula.app.rpc.identity.v1.LookupJoinCodeResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.LookupJoinCodeResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.LookupJoinCodeResponse",
            messageClass = org.aula.app.rpc.identity.v1.LookupJoinCodeResponse::class,
            messageCompanion = this,
            fields = buildList(5) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseId",
                        value = org.aula.app.rpc.identity.v1.LookupJoinCodeResponse::courseId
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseName",
                        value = org.aula.app.rpc.identity.v1.LookupJoinCodeResponse::courseName
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "course_code",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "courseCode",
                        value = org.aula.app.rpc.identity.v1.LookupJoinCodeResponse::courseCode
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "school_name",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "schoolName",
                        value = org.aula.app.rpc.identity.v1.LookupJoinCodeResponse::schoolName
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "student_count",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "studentCount",
                        value = org.aula.app.rpc.identity.v1.LookupJoinCodeResponse::studentCount
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RegisterStudentRequest(
    val joinCode: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.RegisterStudentRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegisterStudentRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.RegisterStudentRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.RegisterStudentRequest by lazy { org.aula.app.rpc.identity.v1.RegisterStudentRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.RegisterStudentRequest = org.aula.app.rpc.identity.v1.RegisterStudentRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegisterStudentRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.RegisterStudentRequest",
            messageClass = org.aula.app.rpc.identity.v1.RegisterStudentRequest::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "join_code",
                        number = 1,
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
                        value = org.aula.app.rpc.identity.v1.RegisterStudentRequest::joinCode
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
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
                        value = org.aula.app.rpc.identity.v1.RegisterStudentRequest::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "email",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "email",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 114, 5, 16, 3, 24, -2, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.identity.v1.RegisterStudentRequest::email
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "password",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "password",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 114, 5, 16, 8, 24, -56, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.identity.v1.RegisterStudentRequest::password
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RegisterStudentResponse(
    val session: org.aula.app.rpc.identity.v1.Session? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.RegisterStudentResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegisterStudentResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.RegisterStudentResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.RegisterStudentResponse by lazy { org.aula.app.rpc.identity.v1.RegisterStudentResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.RegisterStudentResponse = org.aula.app.rpc.identity.v1.RegisterStudentResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegisterStudentResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.RegisterStudentResponse",
            messageClass = org.aula.app.rpc.identity.v1.RegisterStudentResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "session",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.Session.Companion),
                        jsonName = "session",
                        value = org.aula.app.rpc.identity.v1.RegisterStudentResponse::session
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RegisterDeviceRequest(
    val pushToken: String = "",
    val platform: org.aula.app.rpc.identity.v1.DevicePlatform = org.aula.app.rpc.identity.v1.DevicePlatform.fromValue(0),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.RegisterDeviceRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegisterDeviceRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.RegisterDeviceRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.RegisterDeviceRequest by lazy { org.aula.app.rpc.identity.v1.RegisterDeviceRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.RegisterDeviceRequest = org.aula.app.rpc.identity.v1.RegisterDeviceRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegisterDeviceRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.RegisterDeviceRequest",
            messageClass = org.aula.app.rpc.identity.v1.RegisterDeviceRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "push_token",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "pushToken",
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
                        value = org.aula.app.rpc.identity.v1.RegisterDeviceRequest::pushToken
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "platform",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Enum(enumCompanion = org.aula.app.rpc.identity.v1.DevicePlatform.Companion),
                        jsonName = "platform",
                        value = org.aula.app.rpc.identity.v1.RegisterDeviceRequest::platform
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RegisterDeviceResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.RegisterDeviceResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegisterDeviceResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.RegisterDeviceResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.RegisterDeviceResponse by lazy { org.aula.app.rpc.identity.v1.RegisterDeviceResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.RegisterDeviceResponse = org.aula.app.rpc.identity.v1.RegisterDeviceResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegisterDeviceResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.RegisterDeviceResponse",
            messageClass = org.aula.app.rpc.identity.v1.RegisterDeviceResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class Teacher(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val invited: Boolean = false,
    val inviteUrl: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.Teacher = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.Teacher> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.Teacher> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.Teacher by lazy { org.aula.app.rpc.identity.v1.Teacher() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.Teacher = org.aula.app.rpc.identity.v1.Teacher.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.Teacher> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.Teacher",
            messageClass = org.aula.app.rpc.identity.v1.Teacher::class,
            messageCompanion = this,
            fields = buildList(5) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "id",
                        value = org.aula.app.rpc.identity.v1.Teacher::id
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = org.aula.app.rpc.identity.v1.Teacher::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "email",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "email",
                        value = org.aula.app.rpc.identity.v1.Teacher::email
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "invited",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "invited",
                        value = org.aula.app.rpc.identity.v1.Teacher::invited
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "invite_url",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "inviteUrl",
                        value = org.aula.app.rpc.identity.v1.Teacher::inviteUrl
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class ListTeachersRequest(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.ListTeachersRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.ListTeachersRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.ListTeachersRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.ListTeachersRequest by lazy { org.aula.app.rpc.identity.v1.ListTeachersRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.ListTeachersRequest = org.aula.app.rpc.identity.v1.ListTeachersRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.ListTeachersRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.ListTeachersRequest",
            messageClass = org.aula.app.rpc.identity.v1.ListTeachersRequest::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class ListTeachersResponse(
    val teachers: List<org.aula.app.rpc.identity.v1.Teacher> = emptyList(),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.ListTeachersResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.ListTeachersResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.ListTeachersResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.ListTeachersResponse by lazy { org.aula.app.rpc.identity.v1.ListTeachersResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.ListTeachersResponse = org.aula.app.rpc.identity.v1.ListTeachersResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.ListTeachersResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.ListTeachersResponse",
            messageClass = org.aula.app.rpc.identity.v1.ListTeachersResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "teachers",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Repeated<org.aula.app.rpc.identity.v1.Teacher>(valueType = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.Teacher.Companion)),
                        jsonName = "teachers",
                        value = org.aula.app.rpc.identity.v1.ListTeachersResponse::teachers
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class InviteTeacherRequest(
    val name: String = "",
    val email: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.InviteTeacherRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.InviteTeacherRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.InviteTeacherRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.InviteTeacherRequest by lazy { org.aula.app.rpc.identity.v1.InviteTeacherRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.InviteTeacherRequest = org.aula.app.rpc.identity.v1.InviteTeacherRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.InviteTeacherRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.InviteTeacherRequest",
            messageClass = org.aula.app.rpc.identity.v1.InviteTeacherRequest::class,
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
                        value = org.aula.app.rpc.identity.v1.InviteTeacherRequest::name
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "email",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "email",
                        options = pbandk.wkt.FieldOptions(
                            unknownFields = mapOf(
                                1159 to pbandk.UnknownField(
                                    fieldNum = 1159,
                                    values = listOf(
                                        pbandk.UnknownField.Value(wireType = 2, rawBytes = byteArrayOf(7, 114, 5, 16, 3, 24, -2, 1))
                                    )
                                )
                            )
                        ),
                        value = org.aula.app.rpc.identity.v1.InviteTeacherRequest::email
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class InviteTeacherResponse(
    val teacher: org.aula.app.rpc.identity.v1.Teacher? = null,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.InviteTeacherResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.InviteTeacherResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.InviteTeacherResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.InviteTeacherResponse by lazy { org.aula.app.rpc.identity.v1.InviteTeacherResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.InviteTeacherResponse = org.aula.app.rpc.identity.v1.InviteTeacherResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.InviteTeacherResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.InviteTeacherResponse",
            messageClass = org.aula.app.rpc.identity.v1.InviteTeacherResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "teacher",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Message(messageCompanion = org.aula.app.rpc.identity.v1.Teacher.Companion),
                        jsonName = "teacher",
                        value = org.aula.app.rpc.identity.v1.InviteTeacherResponse::teacher
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RegenerateTeacherInviteRequest(
    val teacherId: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest by lazy { org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest = org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.RegenerateTeacherInviteRequest",
            messageClass = org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "teacher_id",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "teacherId",
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
                        value = org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest::teacherId
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class RegenerateTeacherInviteResponse(
    val inviteUrl: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse> {
        public val defaultInstance: org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse by lazy { org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse = org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse> = pbandk.MessageDescriptor(
            fullName = "aula.identity.v1.RegenerateTeacherInviteResponse",
            messageClass = org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "invite_url",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "inviteUrl",
                        value = org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse::inviteUrl
                    )
                )
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForSchool")
public fun School?.orDefault(): org.aula.app.rpc.identity.v1.School = this ?: School.defaultInstance

private fun School.protoMergeImpl(plus: pbandk.Message?): School = (plus as? School)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun School.Companion.decodeWithImpl(u: pbandk.MessageDecoder): School {
    var id = ""
    var name = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> name = _fieldValue as String
        }
    }

    return School(id, name, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForUser")
public fun User?.orDefault(): org.aula.app.rpc.identity.v1.User = this ?: User.defaultInstance

private fun User.protoMergeImpl(plus: pbandk.Message?): User = (plus as? User)?.let {
    it.copy(
        school = school?.plus(plus.school) ?: plus.school,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun User.Companion.decodeWithImpl(u: pbandk.MessageDecoder): User {
    var id = ""
    var name = ""
    var email = ""
    var role: org.aula.app.rpc.identity.v1.Role = org.aula.app.rpc.identity.v1.Role.fromValue(0)
    var school: org.aula.app.rpc.identity.v1.School? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> email = _fieldValue as String
            4 -> role = _fieldValue as org.aula.app.rpc.identity.v1.Role
            5 -> school = _fieldValue as org.aula.app.rpc.identity.v1.School
        }
    }

    return User(id, name, email, role,
        school, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSession")
public fun Session?.orDefault(): org.aula.app.rpc.identity.v1.Session = this ?: Session.defaultInstance

private fun Session.protoMergeImpl(plus: pbandk.Message?): Session = (plus as? Session)?.let {
    it.copy(
        user = user?.plus(plus.user) ?: plus.user,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Session.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Session {
    var accessToken = ""
    var refreshToken = ""
    var user: org.aula.app.rpc.identity.v1.User? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> accessToken = _fieldValue as String
            2 -> refreshToken = _fieldValue as String
            3 -> user = _fieldValue as org.aula.app.rpc.identity.v1.User
        }
    }

    return Session(accessToken, refreshToken, user, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLoginRequest")
public fun LoginRequest?.orDefault(): org.aula.app.rpc.identity.v1.LoginRequest = this ?: LoginRequest.defaultInstance

private fun LoginRequest.protoMergeImpl(plus: pbandk.Message?): LoginRequest = (plus as? LoginRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LoginRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LoginRequest {
    var email = ""
    var password = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> email = _fieldValue as String
            2 -> password = _fieldValue as String
        }
    }

    return LoginRequest(email, password, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLoginResponse")
public fun LoginResponse?.orDefault(): org.aula.app.rpc.identity.v1.LoginResponse = this ?: LoginResponse.defaultInstance

private fun LoginResponse.protoMergeImpl(plus: pbandk.Message?): LoginResponse = (plus as? LoginResponse)?.let {
    it.copy(
        session = session?.plus(plus.session) ?: plus.session,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LoginResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LoginResponse {
    var session: org.aula.app.rpc.identity.v1.Session? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> session = _fieldValue as org.aula.app.rpc.identity.v1.Session
        }
    }

    return LoginResponse(session, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRefreshSessionRequest")
public fun RefreshSessionRequest?.orDefault(): org.aula.app.rpc.identity.v1.RefreshSessionRequest = this ?: RefreshSessionRequest.defaultInstance

private fun RefreshSessionRequest.protoMergeImpl(plus: pbandk.Message?): RefreshSessionRequest = (plus as? RefreshSessionRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RefreshSessionRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RefreshSessionRequest {
    var refreshToken = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> refreshToken = _fieldValue as String
        }
    }

    return RefreshSessionRequest(refreshToken, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRefreshSessionResponse")
public fun RefreshSessionResponse?.orDefault(): org.aula.app.rpc.identity.v1.RefreshSessionResponse = this ?: RefreshSessionResponse.defaultInstance

private fun RefreshSessionResponse.protoMergeImpl(plus: pbandk.Message?): RefreshSessionResponse = (plus as? RefreshSessionResponse)?.let {
    it.copy(
        session = session?.plus(plus.session) ?: plus.session,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RefreshSessionResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RefreshSessionResponse {
    var session: org.aula.app.rpc.identity.v1.Session? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> session = _fieldValue as org.aula.app.rpc.identity.v1.Session
        }
    }

    return RefreshSessionResponse(session, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLogoutRequest")
public fun LogoutRequest?.orDefault(): org.aula.app.rpc.identity.v1.LogoutRequest = this ?: LogoutRequest.defaultInstance

private fun LogoutRequest.protoMergeImpl(plus: pbandk.Message?): LogoutRequest = (plus as? LogoutRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LogoutRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LogoutRequest {
    var refreshToken = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> refreshToken = _fieldValue as String
        }
    }

    return LogoutRequest(refreshToken, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLogoutResponse")
public fun LogoutResponse?.orDefault(): org.aula.app.rpc.identity.v1.LogoutResponse = this ?: LogoutResponse.defaultInstance

private fun LogoutResponse.protoMergeImpl(plus: pbandk.Message?): LogoutResponse = (plus as? LogoutResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LogoutResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LogoutResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return LogoutResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetMeRequest")
public fun GetMeRequest?.orDefault(): org.aula.app.rpc.identity.v1.GetMeRequest = this ?: GetMeRequest.defaultInstance

private fun GetMeRequest.protoMergeImpl(plus: pbandk.Message?): GetMeRequest = (plus as? GetMeRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetMeRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetMeRequest {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return GetMeRequest(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetMeResponse")
public fun GetMeResponse?.orDefault(): org.aula.app.rpc.identity.v1.GetMeResponse = this ?: GetMeResponse.defaultInstance

private fun GetMeResponse.protoMergeImpl(plus: pbandk.Message?): GetMeResponse = (plus as? GetMeResponse)?.let {
    it.copy(
        user = user?.plus(plus.user) ?: plus.user,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetMeResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetMeResponse {
    var user: org.aula.app.rpc.identity.v1.User? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> user = _fieldValue as org.aula.app.rpc.identity.v1.User
        }
    }

    return GetMeResponse(user, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetInvitationRequest")
public fun GetInvitationRequest?.orDefault(): org.aula.app.rpc.identity.v1.GetInvitationRequest = this ?: GetInvitationRequest.defaultInstance

private fun GetInvitationRequest.protoMergeImpl(plus: pbandk.Message?): GetInvitationRequest = (plus as? GetInvitationRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetInvitationRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetInvitationRequest {
    var token = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> token = _fieldValue as String
        }
    }

    return GetInvitationRequest(token, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForGetInvitationResponse")
public fun GetInvitationResponse?.orDefault(): org.aula.app.rpc.identity.v1.GetInvitationResponse = this ?: GetInvitationResponse.defaultInstance

private fun GetInvitationResponse.protoMergeImpl(plus: pbandk.Message?): GetInvitationResponse = (plus as? GetInvitationResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun GetInvitationResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): GetInvitationResponse {
    var schoolName = ""
    var name = ""
    var email = ""
    var role: org.aula.app.rpc.identity.v1.Role = org.aula.app.rpc.identity.v1.Role.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> schoolName = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> email = _fieldValue as String
            4 -> role = _fieldValue as org.aula.app.rpc.identity.v1.Role
        }
    }

    return GetInvitationResponse(schoolName, name, email, role, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForAcceptInvitationRequest")
public fun AcceptInvitationRequest?.orDefault(): org.aula.app.rpc.identity.v1.AcceptInvitationRequest = this ?: AcceptInvitationRequest.defaultInstance

private fun AcceptInvitationRequest.protoMergeImpl(plus: pbandk.Message?): AcceptInvitationRequest = (plus as? AcceptInvitationRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun AcceptInvitationRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): AcceptInvitationRequest {
    var token = ""
    var password = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> token = _fieldValue as String
            2 -> password = _fieldValue as String
        }
    }

    return AcceptInvitationRequest(token, password, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForAcceptInvitationResponse")
public fun AcceptInvitationResponse?.orDefault(): org.aula.app.rpc.identity.v1.AcceptInvitationResponse = this ?: AcceptInvitationResponse.defaultInstance

private fun AcceptInvitationResponse.protoMergeImpl(plus: pbandk.Message?): AcceptInvitationResponse = (plus as? AcceptInvitationResponse)?.let {
    it.copy(
        session = session?.plus(plus.session) ?: plus.session,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun AcceptInvitationResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): AcceptInvitationResponse {
    var session: org.aula.app.rpc.identity.v1.Session? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> session = _fieldValue as org.aula.app.rpc.identity.v1.Session
        }
    }

    return AcceptInvitationResponse(session, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLookupJoinCodeRequest")
public fun LookupJoinCodeRequest?.orDefault(): org.aula.app.rpc.identity.v1.LookupJoinCodeRequest = this ?: LookupJoinCodeRequest.defaultInstance

private fun LookupJoinCodeRequest.protoMergeImpl(plus: pbandk.Message?): LookupJoinCodeRequest = (plus as? LookupJoinCodeRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LookupJoinCodeRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LookupJoinCodeRequest {
    var joinCode = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> joinCode = _fieldValue as String
        }
    }

    return LookupJoinCodeRequest(joinCode, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForLookupJoinCodeResponse")
public fun LookupJoinCodeResponse?.orDefault(): org.aula.app.rpc.identity.v1.LookupJoinCodeResponse = this ?: LookupJoinCodeResponse.defaultInstance

private fun LookupJoinCodeResponse.protoMergeImpl(plus: pbandk.Message?): LookupJoinCodeResponse = (plus as? LookupJoinCodeResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun LookupJoinCodeResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): LookupJoinCodeResponse {
    var courseId = ""
    var courseName = ""
    var courseCode = ""
    var schoolName = ""
    var studentCount = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> courseId = _fieldValue as String
            2 -> courseName = _fieldValue as String
            3 -> courseCode = _fieldValue as String
            4 -> schoolName = _fieldValue as String
            5 -> studentCount = _fieldValue as Int
        }
    }

    return LookupJoinCodeResponse(courseId, courseName, courseCode, schoolName,
        studentCount, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRegisterStudentRequest")
public fun RegisterStudentRequest?.orDefault(): org.aula.app.rpc.identity.v1.RegisterStudentRequest = this ?: RegisterStudentRequest.defaultInstance

private fun RegisterStudentRequest.protoMergeImpl(plus: pbandk.Message?): RegisterStudentRequest = (plus as? RegisterStudentRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RegisterStudentRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RegisterStudentRequest {
    var joinCode = ""
    var name = ""
    var email = ""
    var password = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> joinCode = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> email = _fieldValue as String
            4 -> password = _fieldValue as String
        }
    }

    return RegisterStudentRequest(joinCode, name, email, password, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRegisterStudentResponse")
public fun RegisterStudentResponse?.orDefault(): org.aula.app.rpc.identity.v1.RegisterStudentResponse = this ?: RegisterStudentResponse.defaultInstance

private fun RegisterStudentResponse.protoMergeImpl(plus: pbandk.Message?): RegisterStudentResponse = (plus as? RegisterStudentResponse)?.let {
    it.copy(
        session = session?.plus(plus.session) ?: plus.session,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RegisterStudentResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RegisterStudentResponse {
    var session: org.aula.app.rpc.identity.v1.Session? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> session = _fieldValue as org.aula.app.rpc.identity.v1.Session
        }
    }

    return RegisterStudentResponse(session, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRegisterDeviceRequest")
public fun RegisterDeviceRequest?.orDefault(): org.aula.app.rpc.identity.v1.RegisterDeviceRequest = this ?: RegisterDeviceRequest.defaultInstance

private fun RegisterDeviceRequest.protoMergeImpl(plus: pbandk.Message?): RegisterDeviceRequest = (plus as? RegisterDeviceRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RegisterDeviceRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RegisterDeviceRequest {
    var pushToken = ""
    var platform: org.aula.app.rpc.identity.v1.DevicePlatform = org.aula.app.rpc.identity.v1.DevicePlatform.fromValue(0)

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> pushToken = _fieldValue as String
            2 -> platform = _fieldValue as org.aula.app.rpc.identity.v1.DevicePlatform
        }
    }

    return RegisterDeviceRequest(pushToken, platform, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRegisterDeviceResponse")
public fun RegisterDeviceResponse?.orDefault(): org.aula.app.rpc.identity.v1.RegisterDeviceResponse = this ?: RegisterDeviceResponse.defaultInstance

private fun RegisterDeviceResponse.protoMergeImpl(plus: pbandk.Message?): RegisterDeviceResponse = (plus as? RegisterDeviceResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RegisterDeviceResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RegisterDeviceResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return RegisterDeviceResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForTeacher")
public fun Teacher?.orDefault(): org.aula.app.rpc.identity.v1.Teacher = this ?: Teacher.defaultInstance

private fun Teacher.protoMergeImpl(plus: pbandk.Message?): Teacher = (plus as? Teacher)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun Teacher.Companion.decodeWithImpl(u: pbandk.MessageDecoder): Teacher {
    var id = ""
    var name = ""
    var email = ""
    var invited = false
    var inviteUrl = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> id = _fieldValue as String
            2 -> name = _fieldValue as String
            3 -> email = _fieldValue as String
            4 -> invited = _fieldValue as Boolean
            5 -> inviteUrl = _fieldValue as String
        }
    }

    return Teacher(id, name, email, invited,
        inviteUrl, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListTeachersRequest")
public fun ListTeachersRequest?.orDefault(): org.aula.app.rpc.identity.v1.ListTeachersRequest = this ?: ListTeachersRequest.defaultInstance

private fun ListTeachersRequest.protoMergeImpl(plus: pbandk.Message?): ListTeachersRequest = (plus as? ListTeachersRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListTeachersRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListTeachersRequest {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return ListTeachersRequest(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForListTeachersResponse")
public fun ListTeachersResponse?.orDefault(): org.aula.app.rpc.identity.v1.ListTeachersResponse = this ?: ListTeachersResponse.defaultInstance

private fun ListTeachersResponse.protoMergeImpl(plus: pbandk.Message?): ListTeachersResponse = (plus as? ListTeachersResponse)?.let {
    it.copy(
        teachers = teachers + plus.teachers,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun ListTeachersResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): ListTeachersResponse {
    var teachers: pbandk.ListWithSize.Builder<org.aula.app.rpc.identity.v1.Teacher>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> teachers = (teachers ?: pbandk.ListWithSize.Builder()).apply { this += _fieldValue as kotlin.sequences.Sequence<org.aula.app.rpc.identity.v1.Teacher> }
        }
    }

    return ListTeachersResponse(pbandk.ListWithSize.Builder.fixed(teachers), unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForInviteTeacherRequest")
public fun InviteTeacherRequest?.orDefault(): org.aula.app.rpc.identity.v1.InviteTeacherRequest = this ?: InviteTeacherRequest.defaultInstance

private fun InviteTeacherRequest.protoMergeImpl(plus: pbandk.Message?): InviteTeacherRequest = (plus as? InviteTeacherRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun InviteTeacherRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): InviteTeacherRequest {
    var name = ""
    var email = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> name = _fieldValue as String
            2 -> email = _fieldValue as String
        }
    }

    return InviteTeacherRequest(name, email, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForInviteTeacherResponse")
public fun InviteTeacherResponse?.orDefault(): org.aula.app.rpc.identity.v1.InviteTeacherResponse = this ?: InviteTeacherResponse.defaultInstance

private fun InviteTeacherResponse.protoMergeImpl(plus: pbandk.Message?): InviteTeacherResponse = (plus as? InviteTeacherResponse)?.let {
    it.copy(
        teacher = teacher?.plus(plus.teacher) ?: plus.teacher,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun InviteTeacherResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): InviteTeacherResponse {
    var teacher: org.aula.app.rpc.identity.v1.Teacher? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> teacher = _fieldValue as org.aula.app.rpc.identity.v1.Teacher
        }
    }

    return InviteTeacherResponse(teacher, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRegenerateTeacherInviteRequest")
public fun RegenerateTeacherInviteRequest?.orDefault(): org.aula.app.rpc.identity.v1.RegenerateTeacherInviteRequest = this ?: RegenerateTeacherInviteRequest.defaultInstance

private fun RegenerateTeacherInviteRequest.protoMergeImpl(plus: pbandk.Message?): RegenerateTeacherInviteRequest = (plus as? RegenerateTeacherInviteRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RegenerateTeacherInviteRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RegenerateTeacherInviteRequest {
    var teacherId = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> teacherId = _fieldValue as String
        }
    }

    return RegenerateTeacherInviteRequest(teacherId, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForRegenerateTeacherInviteResponse")
public fun RegenerateTeacherInviteResponse?.orDefault(): org.aula.app.rpc.identity.v1.RegenerateTeacherInviteResponse = this ?: RegenerateTeacherInviteResponse.defaultInstance

private fun RegenerateTeacherInviteResponse.protoMergeImpl(plus: pbandk.Message?): RegenerateTeacherInviteResponse = (plus as? RegenerateTeacherInviteResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun RegenerateTeacherInviteResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): RegenerateTeacherInviteResponse {
    var inviteUrl = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> inviteUrl = _fieldValue as String
        }
    }

    return RegenerateTeacherInviteResponse(inviteUrl, unknownFields)
}
