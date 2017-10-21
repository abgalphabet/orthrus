package orthrus.web.handlers

import ratpack.func.Action
import ratpack.handling.Chain

/**
 * Created by arthur on 14/7/2017.
 */
class Handlers : Action<Chain> {
    override fun execute(chain: Chain) {
        chain.all(ProfileHandler::class.java)
                .get("conf", ConfHandler::class.java)
                .path("annotated/:id?", AnnotatedHandler::class.java)
                .prefix("prefix") {
                    it
                            .get("get") { ctx -> ctx.render("get /prefix/get") }
                            .post("post") { ctx -> ctx.render("post /prefix/post") }
                }
    }
}