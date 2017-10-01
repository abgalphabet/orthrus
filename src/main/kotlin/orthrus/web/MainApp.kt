package orthrus.web

import orthrus.conf.MainModule
import ratpack.guice.Guice
import ratpack.server.RatpackServer
import ratpack.server.RatpackServerSpec
import orthrus.web.handlers.Handlers

/**
 * Created by arthur on 14/7/2017.
 */
class MainApp: App {
    val server: RatpackServer

    init {
        val injector = com.google.inject.Guice.createInjector(MainModule())
        server = RatpackServer.of({ serverSpec: RatpackServerSpec ->
            serverSpec
                .registry(Guice.registry(injector))
                .handlers(Handlers())
        })
    }

    override fun start() {
        server.start()
    }

    override fun stop() {
        server.stop()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            MainApp().start()
        }
    }
}

