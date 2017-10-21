package orthrus.web.handlers

import com.google.inject.Inject
import com.typesafe.config.Config
import orthrus.*
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.session.Session
import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.functions
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.javaType

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

        if (function != null) {
            val permissionAnnotation = function.annotations.find { it.annotationClass.java.simpleName == "HasPermission" }
            val hasPermission = permissionAnnotation as HasPermission
            val permission: Array<Permission> = hasPermission.permissions
            session.data.then { data ->
                val grant: Optional<Array<Permission>> = data.get("permissions") as Optional<Array<Permission>>
                if (grant.isPresent && grant.get().contains(permission.first())) {
                    val parameters = function.parameters.map { p ->
                        when {
                            p.kind == KParameter.Kind.INSTANCE -> p to this
                            p.type == Context::class.starProjectedType -> p to ctx
                            else -> p to asTypedValue(ctx.pathTokens[p.name], p)
                        }
                    }.toMap()
                    function!!.callBy(parameters)
                } else
                    ctx.response.status(ratpack.http.Status.of(403)).send()
            }

        } else ctx.next()
    }


    private fun asTypedValue(param: String?, p: KParameter): Any? = when (p.type.javaType) {
        Int::class.java -> param?.toIntOrNull()
        Long::class.java -> param?.toLongOrNull()
        Boolean::class.java -> param?.toBoolean()
        else -> param
    }


    @Get
    @HasPermission(arrayOf(Permission.All))
    fun get(ctx: Context) {
        ctx.render(conf.getString("app.env"))
    }

    @Post
    @HasPermission(arrayOf(Permission.All))
    fun post(ctx: Context) {
        ctx.render(conf.getString("app.env"))
    }

    @Put
    @HasPermission(arrayOf(Permission.All))
    fun put(ctx: Context) {
        ctx.render(conf.getString("app.env"))
    }

    @Delete
    @HasPermission(arrayOf(Permission.All))
    fun delete(ctx: Context, id: Int) {
        ctx.render("Resourse $id is deleted.")
    }
}