package org.aula.app.rpc

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.timeout
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.readByteArray
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.io.EOFException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import pbandk.Message
import pbandk.decodeFromByteArray
import pbandk.encodeToByteArray

/** Who is calling and for which school. Every authenticated request carries both (Authorization + X-School-Id). */
interface SessionHeaders {
    fun accessToken(): String?
    fun schoolId(): String?
    /** Called once when a request fails with TOKEN_EXPIRED. Return true after refreshing, to retry. */
    suspend fun refresh(): Boolean = false
}

/**
 * [RpcTransport] over the Connect protocol. No Connect client library is needed (none supports Kotlin/Native):
 * unary is `POST /{package}.{Service}/{Method}` with an `application/proto` body; server streaming is the
 * same path with `application/connect+proto` and 5-byte envelopes (1 flags byte + 4 bytes big-endian length).
 * Binary protobuf on purpose: pbandk's JSON codec is experimental and throws on unknown enum names, which
 * would crash the app the first time the server ships a value this build does not know.
 *
 * UNVERIFIED on iOS (Darwin engine) and on wasm: streaming response bodies must arrive incrementally there.
 * The polling fallback (refetchEvery on GetLessonSnapshot) exists for that reason.
 */
class ConnectTransport(
    private val httpClient: HttpClient,
    private val baseUrl: String,
    private val session: SessionHeaders? = null,
) : RpcTransport {

    private val json = Json { ignoreUnknownKeys = true }
    private fun url(procedure: String) = "${baseUrl.trimEnd('/')}/$procedure"

    override suspend fun <Req : Message, Res : Message> unary(procedure: String, request: Req, responseCompanion: Message.Companion<Res>): Res {
        val response = try {
            try {
                send(procedure, request)
            } catch (e: ConnectException) {
                // One refresh-and-retry when the access token expired.
                if (e.domainCode == "TOKEN_EXPIRED" && session?.refresh() == true) send(procedure, request) else throw e
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw ConnectException(ConnectCode.UNAVAILABLE, e.message ?: "no se pudo contactar al servidor", 0)
        }
        return responseCompanion.decodeFromByteArray(response)
    }

    /** Sends one unary call, returning the body or throwing the mapped [ConnectException]. */
    private suspend fun <Req : Message> send(procedure: String, request: Req): ByteArray {
        val response = httpClient.post(url(procedure)) {
            contentType(ContentType("application", "proto"))
            header("Connect-Protocol-Version", "1")
            authHeaders()
            setBody(request.encodeToByteArray())
        }
        val body = response.bodyAsBytes()
        if (!response.status.isSuccess()) throw parseError(body, response.status.value, response.headers[ERROR_CODE_HEADER])
        return body
    }

    override fun <Req : Message, Res : Message> serverStream(procedure: String, request: Req, responseCompanion: Message.Companion<Res>): Flow<Res> = flow {
        try {
            httpClient.preparePost(url(procedure)) {
                // A stream lives as long as the screen does: no request/socket timeout (needs HttpTimeout installed, see AppContainer).
                timeout { requestTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS; socketTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS }
                contentType(ContentType("application", "connect+proto"))
                header("Connect-Protocol-Version", "1")
                authHeaders()
                setBody(envelope(0, request.encodeToByteArray()))
            }.execute { response ->
                if (!response.status.isSuccess()) throw parseError(response.bodyAsBytes(), response.status.value, response.headers[ERROR_CODE_HEADER])
                val channel = response.bodyAsChannel()
                while (true) {
                    val head = try { channel.readByteArray(5) } catch (_: EOFException) { break }
                    val flags = head[0].toInt()
                    val length = ((head[1].toInt() and 0xFF) shl 24) or ((head[2].toInt() and 0xFF) shl 16) or ((head[3].toInt() and 0xFF) shl 8) or (head[4].toInt() and 0xFF)
                    val payload = if (length == 0) ByteArray(0) else channel.readByteArray(length)
                    if (flags and END_STREAM != 0) {
                        val end = runCatching { json.decodeFromString<EndStreamBody>(payload.decodeToString()) }.getOrNull()
                        end?.error?.let { throw ConnectException(ConnectCode.fromWireName(it.code), it.message ?: "stream error", response.status.value) }
                        break
                    }
                    emit(responseCompanion.decodeFromByteArray(payload))
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            throw ConnectException(ConnectCode.UNAVAILABLE, e.message ?: "se perdió la conexión", 0)
        }
    }

    private fun io.ktor.client.request.HttpRequestBuilder.authHeaders() {
        session?.accessToken()?.let { header("Authorization", "Bearer $it") }
        session?.schoolId()?.let { header("X-School-Id", it) }
    }

    private fun envelope(flags: Int, payload: ByteArray): ByteArray {
        val out = ByteArray(5 + payload.size)
        out[0] = flags.toByte()
        out[1] = (payload.size ushr 24).toByte(); out[2] = (payload.size ushr 16).toByte()
        out[3] = (payload.size ushr 8).toByte(); out[4] = payload.size.toByte()
        payload.copyInto(out, 5)
        return out
    }

    /** Connect errors are JSON even for binary requests. A proxy can answer with HTML: a parse failure must not mask the status. */
    private fun parseError(body: ByteArray, httpStatus: Int, domainCode: String?): ConnectException {
        val text = body.decodeToString()
        val parsed = try { json.decodeFromString<ConnectErrorBody>(text) } catch (_: SerializationException) { null }
        return ConnectException(
            code = ConnectCode.fromWireName(parsed?.code),
            message = parsed?.message ?: text.ifBlank { "HTTP $httpStatus" },
            httpStatus = httpStatus,
            domainCode = domainCode?.takeIf { it.isNotBlank() },
        )
    }

    private companion object {
        const val ERROR_CODE_HEADER = "Aula-Error-Code"
        const val END_STREAM = 0x02
    }
}
