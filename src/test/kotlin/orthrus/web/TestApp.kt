package orthrus.web

import com.google.inject.Injector
import com.google.inject.util.Modules
import orthrus.conf.MainModule
import orthrus.conf.TestModule
import ratpack.guice.Guice
import ratpack.http.client.ReceivedResponse
import ratpack.server.RatpackServer
import ratpack.server.RatpackServerSpec
import ratpack.test.embed.EmbeddedApp
import ratpack.test.http.TestHttpClient
import orthrus.web.handlers.Handlers

/**
 * Created by arthur on 17/6/2017.
 */

/**
 * Created by arthur on 14/7/2017.
 */
class TestApp: App {
    val server: RatpackServer

    val injector : Injector
    val httpclient: TestHttpClient

    init {
        injector = com.google.inject.Guice.createInjector(Modules.override(MainModule()).with(TestModule()))
        val app = EmbeddedApp.of { serverSpec: RatpackServerSpec ->
                serverSpec
                    .registry(Guice.registry(injector))
                    .handlers(Handlers())
            }
        server = app.server
        httpclient = app.httpClient
    }

    override fun start() {
        server.start()
    }

    override fun stop() {
        server.stop()
    }

    private fun test(testSpec: (TestHttpClient) -> Unit) {
        testSpec(httpclient)
    }

    fun get(path: String, doAssertions: (ReceivedResponse) -> Unit) {
        this.test({ testHttpClient ->
            doAssertions(testHttpClient.get(path))
        })
    }

    fun post(path: String, payload: String, doAssertions: (ReceivedResponse) -> Unit) {
        this.test({ testHttpClient ->
            val receivedResponse = testHttpClient.requestSpec { s ->
                s.body { b -> b.type("application/json").text(payload) }
            }.post(path)

            doAssertions(receivedResponse)
        })
    }
}