package bsync.banks.plaid

import bsync.myhttp.WsClientSessionI
import bsync.myhttp.WsRequestMessage


interface PlaidServiceI {
  
    suspend fun access_create(clientSession: WsClientSessionI,
                              publicAccessKey: String,
                              finsyncProfileId: Int,
                              bankName: String?,
                              bankUid: String?)
}
