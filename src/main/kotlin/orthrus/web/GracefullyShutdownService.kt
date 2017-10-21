package orthrus.web

import kotlin.concurrent.thread

abstract class GracefullyShutdownService : Service {
    init {
        Runtime.getRuntime().addShutdownHook(thread(start = false) { this.stop() })
    }
}