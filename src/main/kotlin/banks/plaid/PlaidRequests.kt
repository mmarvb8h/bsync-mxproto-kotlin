package bsync.banks.plaid

import kotlinx.serialization.*
import java.util.Date


@Serializable
data class PlaidReqRange(
    val startDate: String,
    val numMonths: Int)

@Serializable
data class BankSyncAccount(
    val id: String,
    val name: String? = null,
    val subtype: String? = null)

@Serializable
data class BankSyncAccountGroup(
    val syncRange: PlaidReqRange,
    val accounts: List<BankSyncAccount>)

@Serializable
data class BankSync(
    val institutionId: String,
    val institutionName: String? = null,
    val linkToken: String,
    val accountSyncGroup: BankSyncAccountGroup)
