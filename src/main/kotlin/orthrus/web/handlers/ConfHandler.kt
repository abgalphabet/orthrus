package orthrus.web.handlers

import com.google.inject.Inject
import com.typesafe.config.Config
import ratpack.handling.Context
import ratpack.handling.Handler

/**
 * Created by arthur on 17/6/2017.
 */
class ConfHandler @Inject
constructor(val conf: Config) : Handler {
    override fun handle(ctx: Context) {
        ctx.render(conf.getString("app.env"))
    }
}


