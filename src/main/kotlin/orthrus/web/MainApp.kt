package orthrus.web

import io.netty.handler.ssl.ClientAuth
import io.netty.handler.ssl.SslContextBuilder
import orthrus.conf.MainModule
import orthrus.web.handlers.Handlers
import ratpack.guice.Guice
import ratpack.server.RatpackServer
import ratpack.ssl.internal.SslContexts
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.TrustManagerFactory

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
                    val keystore = MainApp.javaClass.getResource("/pki/keystore.jks")
                    val truststore = MainApp.javaClass.getResource("/pki/truststore.jks")

                    val keyManagerFactory = SslContexts.keyManagerFactory(keystore.openStream(), "passw0rd".toCharArray())
                    val trustManagerFactory = SslContexts.trustManagerFactory(truststore.openStream(), "passw0rd".toCharArray())

                    val sslContext = SslContextBuilder
                        .forServer(keyManagerFactory)
                        .trustManager(trustManagerFactory)
                        .clientAuth(ClientAuth.REQUIRE)
                        .build()

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

