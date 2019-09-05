package bsync

import org.koin.dsl.module
import bsync.banking.mx.*
import bsync.myhttp.MyClientSessionI

// As of 2019-05-22. See doc below:
// https://insert-koin.io/docs/2.0/documentation/koin-core/index.html#_writing_a_module_2

val mxModule = module {

    single<PlaidServiceI> { PlaidService() }
    single<MxUserCreateService> { MxUserCreateService() }
    single<MxUserConnectService> { MxUserConnectService() }
    single<MxClientFactory> { MxClientFactory() }
//    single<AccessDb> { (clientSession: WsClientSessionI) ->
//        AccessDb(clientSession = clientSession) }
//    factory<PlaidTrans> {
//                (client: MyPlaidClientXtend,
//                exchangeToken: MyItemPubTokenExchangeReq,
//                clientSession: WsClientSessionI) ->
//                    PlaidTrans(client = client,
//                        exchangeToken = exchangeToken,
//                        clientSession = clientSession) }
//    factory<PlaidTransToSchema> { (clientSession: WsClientSessionI) ->
//        PlaidTransToSchema(clientSession = clientSession) }
}
