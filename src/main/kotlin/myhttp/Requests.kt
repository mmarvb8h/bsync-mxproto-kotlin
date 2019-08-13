package bsync.myhttp

import kotlinx.serialization.*


@Serializable
data class WsRequestMessage(
    val messageKind: String? = null,
    val responseTo: String? = null,
    val body: String? = null)

@Serializable
data class WsRequest(
    val authorization: String? = null,
    val clientIdentifier: String? = null,
    val sendTo: String? = null,
    val message: WsRequestMessage? = null)
