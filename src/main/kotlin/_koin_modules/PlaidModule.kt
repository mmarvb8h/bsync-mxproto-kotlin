package bsync

import org.koin.dsl.module
import bsync.banks.plaid.*
import bsync.myhttp.WsClientSessionI

// As of 2019-05-22. See doc below:
// https://insert-koin.io/docs/2.0/documentation/koin-core/index.html#_writing_a_module_2

val plaidModule = module {

    single<PlaidServiceI> { PlaidService() }
    single<BsyncStart> { BsyncStart() }
    single<PlaidClientFactory> { PlaidClientFactory() }
    single<AccessToken> { AccessToken() }
    factory<PlaidTransaction> {
                (client: MyPlaidClientXtend,
                exchangeToken: MyItemPubTokenExchangeReq,
                clientSession: WsClientSessionI) ->
                    PlaidTransaction(client = client,
                        exchangeToken = exchangeToken,
                        clientSession = clientSession) }
    factory<PlaidTransactionDbModel> { (clientSession: WsClientSessionI) ->
        PlaidTransactionDbModel(clientSession = clientSession) }
}
