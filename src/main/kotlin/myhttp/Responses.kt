package bsync.myhttp

import kotlinx.serialization.*


@Serializable
data class WsResponseMessage(
    val messageKind: String? = null,
    val respondTo: String? = null,
    val body: String? = null)

@Serializable
data class WsResponse(
    val authorization: String? = null,
    val sendTo: String? = null,
    val message: WsResponseMessage? = null)

@Serializable
data class WhoAmIMessage(
    val identification: String? = null)

