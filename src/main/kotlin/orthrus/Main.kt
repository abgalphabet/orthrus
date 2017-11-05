package orthrus

import io.netty.handler.ssl.ClientAuth
import io.netty.handler.ssl.SslContextBuilder
import orthrus.conf.MainModule
import orthrus.web.handlers.Handlers
import ratpack.guice.Guice
import ratpack.server.RatpackServer
import ratpack.session.SessionModule
import ratpack.ssl.internal.SslContexts


/**
 * Created by arthur on 14/7/2017.
 */
class Main : Startable, Stoppable {
    val http: RatpackServer
    val https: RatpackServer
    val mongodb: EmbeddedMongoDB

    init {
        val injector = com.google.inject.Guice.createInjector(MainModule())
        val registry = Guice.registry(injector) { it.module(SessionModule()) }
        http = RatpackServer.of { serverSpec ->
            serverSpec
                .registry(registry)
                .handlers(Handlers())
                .serverConfig { it.port(5080) }
        }

        https = RatpackServer.of { serverSpec ->
            serverSpec
                .registry(registry)
                .handlers(Handlers())
                .serverConfig { serverConfigBuilder ->
                    val keystore = Main.javaClass.getResource("/pki/keystore.jks")
                    val truststore = Main.javaClass.getResource("/pki/trust-foo-only.jks")

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

        mongodb = injector.getInstance(EmbeddedMongoDB::class.java)
    }

    override fun start() {
        http.start()
        https.start()
        mongodb.start()
    }

    override fun stop() {
        http.stop()
        https.stop()
        mongodb.stop()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main().start()
        }
    }
}

