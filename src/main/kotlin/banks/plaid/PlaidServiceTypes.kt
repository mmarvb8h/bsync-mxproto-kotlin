package bsync.banks.plaid

import okhttp3.ResponseBody
import java.util.Date


data class MyItemPubTokenExchangeReq(
    val accessToken: String,
    val itemId: String,
    val requestId: String)

data class GetTransactionParams(
    val accountIds: List<String>,
    val startDate: Date,
    val endDate: Date,
    val offSet: Int? = null,
    val numPerPage: Int? = null)

data class MyPlaidResponse<T>(
    val code: Int,
    val isSuccessful: Boolean,
    val body: T,
    val message: String,
    val errorBody: ResponseBody?
)
