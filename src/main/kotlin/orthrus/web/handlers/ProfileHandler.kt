package orthrus.web.handlers

import orthrus.Permission
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.session.Session

class ProfileHandler : Handler {
    override fun handle(ctx: Context) {
        val session = ctx.get(Session::class.java)
        val id = session.id
        println(id)
        val certificate = ctx.request.clientCertificate
        if (certificate.isPresent) {
            session.data.then { data ->
                data.set("permissions", arrayOf(Permission.All))
                ctx.next()
            }
        }
        else
            ctx.next()

    }
}