package bsync.banks.plaid

import com.plaid.client.*
//import com.plaid.client.request.*


class PlaidClientFactory {

    fun create (clientId: String, secretKey: String) : MyPlaidClient {

        return MyPlaidClient(
            PlaidClient.newBuilder()
                .clientIdAndSecret(clientId, secretKey)
                .sandboxBaseUrl()
                .build() )
    }

    fun createWithXtendAccess (clientId: String, secretKey: String, publicKey: String) : MyPlaidClientXtend {

        return MyPlaidClientXtend(
            plaidClient = PlaidClient.newBuilder()
                .clientIdAndSecret(clientId, secretKey)
                .publicKey(publicKey)
                .sandboxBaseUrl()
                .build(),
            publicKey = publicKey)
    }
}

open class MyPlaidClient  (val plaidClient: PlaidClient)

class MyPlaidClientXtend (plaidClient: PlaidClient, val publicKey: String) :
    MyPlaidClient (plaidClient = plaidClient)

