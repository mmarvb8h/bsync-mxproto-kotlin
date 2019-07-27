package bsync.banks.plaid

import bsync.myhttp.WsClientSessionI
import bsync.myhttp.WsRequestMessage


interface PlaidServiceI {
    suspend fun performTheService(clientSession: WsClientSessionI, request: WsRequestMessage?)
}
