package orthrus.web

import orthrus.conf.MainModule
import orthrus.web.handlers.Handlers
import ratpack.guice.Guice
import ratpack.server.RatpackServer
import ratpack.ssl.SSLContexts

/**
 * Created by arthur on 14/7/2017.
 */
class MainApp: App {
    val http: RatpackServer
    val https: RatpackServer

    init {
        val injector = com.google.inject.Guice.createInjector(MainModule())
        http = RatpackServer.of { serverSpec ->
            serverSpec
                .registry(Guice.registry(injector))
                .handlers(Handlers())
                .serverConfig {
                    it.port(5080)
                }
        }

        https = RatpackServer.of { serverSpec ->
            serverSpec
                .registry(Guice.registry(injector))
                .handlers(Handlers())
                .serverConfig { serverConfigBuilder ->
                    val keystore = MainApp.javaClass.getResource("/pki/keystore.p12")
                    val truststore = MainApp.javaClass.getResource("/pki/truststore.p12")

                    val sslContext = SSLContexts.sslContext(keystore, "passw0rd", truststore, "passw0rd")
                    serverConfigBuilder
                        .ssl(sslContext)
                        .port(5443)
                }
        }
    }

    override fun start() {
        http.start()
        https.start()
    }

    override fun stop() {
        http.stop()
        https.stop()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            MainApp().start()
        }
    }
}

