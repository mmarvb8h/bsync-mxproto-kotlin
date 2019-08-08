package bsync.banks.plaid

import com.plaid.client.response.TransactionsGetResponse
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*


class GetTransactionPager(
    val accountIdList: List<String>,
    val startDate: Date,
    val numMonths: Int) {

    val start = fun () :GetTransactionParams {
        // Setup date range. Set ending date.
        val ft = SimpleDateFormat("yyyy-MM-dd")
        val calender = Calendar.getInstance()
        // Get date from JSON request.
        calender.time = this.startDate
        // Increment from start date using num months.
        calender.add(Calendar.MONTH, this.numMonths)

        return GetTransactionParams(
            accountIds = this.accountIdList,
            startDate = this.startDate,
            endDate = calender.time
        )
    }

    val nextPage = fun (currentParams: GetTransactionParams, data1: TransactionsGetResponse) : GetTransactionParams {

        val calcOffset = currentParams.offSet ?: 0 + data1.transactions.count()
        if (calcOffset >= data1.totalTransactions) {
            return GetTransactionParams(
                accountIds = currentParams.accountIds,
                startDate = currentParams.startDate,
                endDate = currentParams.endDate,
                offSet = -1,
                numPerPage = -1
            )
        }
        return GetTransactionParams(
            accountIds = currentParams.accountIds,
            startDate = currentParams.startDate,
            endDate = currentParams.endDate,
            offSet = calcOffset,
            numPerPage = 200
        )
    }

    val hasNextPage = { currentParams: GetTransactionParams -> currentParams.offSet ?: 0 > -1 }
}
