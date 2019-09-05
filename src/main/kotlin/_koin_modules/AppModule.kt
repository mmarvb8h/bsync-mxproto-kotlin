package bsync

import io.ktor.application.Application
import org.koin.dsl.module


//import org.koin.experimental.builder.single
//import org.koin.experimental.builder.singleBy

// As of 2019-05-22. See doc below:
// https://insert-koin.io/docs/2.0/documentation/koin-core/index.html#_writing_a_module_2

val appModule = module {
    // single<WebSocketHandler>()
    single<WebRouter> { (app: Application) -> WebRouter(app) }
    single<WsMessage> { WsMessage() }
    single<WhoAmI> { WhoAmI() }
    single<MxuserEndpoints> { MxuserEndpoints() }
    //single<WebSocketHandler> { WebSocketHandler() }
}
