package orthrus.web.handlers

import ratpack.func.Action
import ratpack.handling.Chain
import java.time.ZonedDateTime

/**
 * Created by arthur on 14/7/2017.
 */
class Handlers : Action<Chain> {
    override fun execute(chain: Chain) {
        chain
            .get("conf", ConfHandler::class.java)
    }
}