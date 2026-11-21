package org.aula.app.platform

/**
 * Where the API is, per platform, for development. Override by passing a base URL to `App(...)`.
 * (Android emulator reaches the host as 10.0.2.2; a real phone needs the machine's LAN IP.)
 */
expect val defaultApiBaseUrl: String

/** Milliseconds since the epoch. Used by the query cache for staleness. */
expect fun currentTimeMillis(): Long
