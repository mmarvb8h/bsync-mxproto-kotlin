package bsync.myhttp

import io.ktor.application.*
import io.ktor.request.port
import io.ktor.request.*
import io.ktor.util.pipeline.*
import io.ktor.util.*


class ConnectorInspect (configuration: Configuration) {

    class Configuration {
        // Mutable properties with default values so user can modify.
        var webSocketPort = 0
        var webAppPort = 0
    }
    // Get an immutable snapshot of configuration values.
    val webSocketPort = configuration.webSocketPort
    val webAppPort = configuration.webAppPort

    // Body of feature.
    private fun intercept(context: PipelineContext<Unit, ApplicationCall>) {
        // Do my 'thing'...
        when (context.call.request.uri) {

            "/ws" -> {
                if (context.call.request.port() != webSocketPort) {
                    // Error. Kill pipeline.
                    context.application.log.info("websocket request on incorrect port.")
                    return context.finish()
                }
            }

            else -> {
                if (context.call.request.port() != webAppPort) {
                    // Error. Stop pipeline here.
                    context.application.log.info("application request on incorrect port.")
                    return context.finish()
                }

            }

        }
    }

    //Allows feature to be installed.
    companion object Feature : ApplicationFeature<ApplicationCallPipeline, ConnectorInspect.Configuration, ConnectorInspect> {
        override val key = AttributeKey<ConnectorInspect>(name = "ConnectorInspect")

        override fun install(pipeline: ApplicationCallPipeline, configure: Configuration.() -> Unit): ConnectorInspect {

            // Create new Configuration instance and run lambda user entered
            // when they install(feature) { lambda setting properties }.
            val config = Configuration().apply(configure)
            // Create feature instance.
            val myFeature = ConnectorInspect(configuration = config)
            // Put feature in the application pipeline during the features phase.
            pipeline.intercept(ApplicationCallPipeline.Features) {
                myFeature.intercept(context = this)
            }

            return myFeature
        }
    }


}