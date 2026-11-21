package org.aula.app.rpc

import kotlinx.coroutines.flow.Flow
import pbandk.Message

/**
 * The entire RPC surface the app depends on: unary and server-streaming. Generated clients
 * (`XxxServiceClient`) call this; nothing else in the app talks HTTP.
 */
interface RpcTransport {
    /** @param procedure `aula.courses.v1.CoursesService/ListCourses` (no leading slash). @throws ConnectException */
    suspend fun <Req : Message, Res : Message> unary(procedure: String, request: Req, responseCompanion: Message.Companion<Res>): Res

    /**
     * Server streaming (Connect envelope framing). Cold flow: the request is sent when collected, and the
     * stream ends when the flow is cancelled or the server ends it. Errors arrive as [ConnectException].
     */
    fun <Req : Message, Res : Message> serverStream(procedure: String, request: Req, responseCompanion: Message.Companion<Res>): Flow<Res>
}
