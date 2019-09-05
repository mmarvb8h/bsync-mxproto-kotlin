package bsync.banking.mx

import kotlinx.coroutines.*

import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.text.SimpleDateFormat

import bsync.myhttp.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import com.mx.atrium.*
import com.mx.model.*
import com.mx.model.UserResponseBody




@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
class MxUserConnectService : KoinComponent {

    suspend fun begin(clientSession: MyClientSessionI,
                      user: User) : ConnectWidget? {

        val clientFactory: MxClientFactory by inject()
        val client = clientFactory.getClient()

        val options = ConnectWidgetRequestBody()

        clientSession.log.debug("Call MX.")
        return getMxConnectUrl(client = client, userGuid = user.guid, options = options)
    }

    suspend fun getMxConnectUrl(client: AtriumClient, userGuid: String, options: ConnectWidgetRequestBody) : ConnectWidget? =
        withContext(
            context = Dispatchers.IO + CoroutineName("saveInMx")
        ) {

                // Call MX.
                val response = client.connectWidget.getConnectWidget(userGuid, options)
                return@withContext response.user
        }
}

