package bsync.banks.plaid

import kotlinx.serialization.*
import java.util.Date


@Serializable
data class PlaidReqRange(
    val startDate: String,
    val numMonths: Int)

@Serializable
data class TransSyncAccount(
    val id: String,
    val name: String? = null,
    val subtype: String? = null)

@Serializable
data class TransSyncAccountGroup(
    val syncRange: PlaidReqRange,
    val accounts: List<TransSyncAccount>)

@Serializable
data class TransSync(
    val institutionId: String,
    val institutionName: String? = null,
    val linkToken: String,
    val accountSyncGroup: TransSyncAccountGroup)
