package orthrus

import kotlin.concurrent.thread

abstract class GracefullyShutdownService : Stoppable {
    init {
        Runtime.getRuntime().addShutdownHook(thread(start = false) { this.stop() })
    }
}