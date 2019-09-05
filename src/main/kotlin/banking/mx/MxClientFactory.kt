package bsync.banking.mx

import com.mx.atrium.AtriumClient


class MxClientFactory {

    val mxApiKey = "9f6a758495f01728ff8f444101316901c65d0656"
    val mxClientId = "083a652a-10f0-43ad-92d9-ddfdd919ae61"

    fun getClient () : AtriumClient {

        return AtriumClient(mxApiKey, mxClientId)
    }
}

