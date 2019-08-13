package bsync

import org.koin.dsl.module
import bsync.banks.plaid.*
import bsync.myhttp.WsClientSessionI

// As of 2019-05-22. See doc below:
// https://insert-koin.io/docs/2.0/documentation/koin-core/index.html#_writing_a_module_2

val plaidModule = module {

    single<PlaidServiceI> { PlaidService() }
    single<TransSyncStart> { TransSyncStart() }
    single<PlaidClientFactory> { PlaidClientFactory() }
    single<AccessToken> { AccessToken() }
    factory<PlaidTrans> {
                (client: MyPlaidClientXtend,
                exchangeToken: MyItemPubTokenExchangeReq,
                clientSession: WsClientSessionI) ->
                    PlaidTrans(client = client,
                        exchangeToken = exchangeToken,
                        clientSession = clientSession) }
    factory<PlaidTransToSchema> { (clientSession: WsClientSessionI) ->
        PlaidTransToSchema(clientSession = clientSession) }
}
