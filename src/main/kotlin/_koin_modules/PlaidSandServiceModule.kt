//package bsync
//
//import org.koin.dsl.module
//
//import bsync.myhttp.*
//import bsync.sync.plaid.*

// As of 2019-05-22. See doc below:
// https://insert-koin.io/docs/2.0/documentation/koin-core/index.html#_writing_a_module_2

//val plaidSandServiceModule = module {
//
//    factory<PlaidClientServiceI> { (plaidClientId: String, plaidSecretKey: String) ->
//        PlaidClientService(clientId = plaidClientId, secretKey = plaidSecretKey) }
//    factory<PlaidClientServiceExtendI> { (plaidClientId: String, plaidSecretKey: String, plaidPubKey: String) ->
//        PlaidClientServiceExtend(clientId = plaidClientId, secretKey = plaidSecretKey, publicKey = plaidPubKey) }
//}
