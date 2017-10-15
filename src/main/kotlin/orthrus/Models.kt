package orthrus

import ratpack.http.HttpMethod

@Target(AnnotationTarget.FUNCTION)
annotation class Get

@Target(AnnotationTarget.FUNCTION)
annotation class Post

@Target(AnnotationTarget.FUNCTION)
annotation class HasRole(val roles: Array<Role>)

@Target(AnnotationTarget.FUNCTION)
annotation class HasPermission(val permissions: Array<Permission>)

enum class Role {
    User, SuperUser
}

enum class Permission {
    Limited, All
}
