package orthrus.web.handlers

import com.google.inject.Inject
import com.sun.org.apache.xerces.internal.util.Status
import com.typesafe.config.Config
import orthrus.*
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.session.Session
import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.functions

/**
 * Created by arthur on 17/6/2017.
 */
class AnnotatedHandler @Inject
constructor(val conf: Config) : Handler {
    override fun handle(ctx: Context) {
        val klass = this::class
        val method = ctx.request.method
        val session = ctx.get(Session::class.java)
        val function: KFunction<*>? = klass.functions.find { f ->
            f.annotations.any { it.annotationClass.java.simpleName.toUpperCase() == method.name }
        }

        if (function != null ) {
            val permissionAnnotation = function.annotations.find { it.annotationClass.java.simpleName == "HasPermission" }
            val hasPermission = permissionAnnotation as HasPermission
            val permission: Array<Permission> = hasPermission.permissions
            session.data.then { data ->
                val grant: Optional<Array<Permission>> = data.get("permissions") as Optional<Array<Permission>>
                if (grant.isPresent && grant.get().contains(permission.first()))
                    function!!.call(this, ctx)
                else
                    ctx.response.status(ratpack.http.Status.of(403)).send()
            }

        }
        else ctx.next()
    }

    @Get @HasPermission(arrayOf(Permission.All))
    fun get(ctx: Context) {
        ctx.render(conf.getString("app.env"))
    }
}