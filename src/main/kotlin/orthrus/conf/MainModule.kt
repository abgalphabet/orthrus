package orthrus.conf

import com.google.inject.*
import com.mongodb.MongoClient
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import orthrus.EmbeddedMongoDB
import orthrus.web.handlers.AnnotatedHandler
import orthrus.web.handlers.ConfHandler
import orthrus.web.handlers.ProfileHandler

class MainModule : AbstractModule() {

    override fun configure() {
        bind(ConfHandler::class.java)
        bind(AnnotatedHandler::class.java)
        bind(ProfileHandler::class.java)
        bind(EmbeddedMongoDB::class.java).`in`(Scopes.SINGLETON)
    }

    @Provides @Singleton
    fun config(): Config {
        return ConfigFactory.load("application.prod.conf")
    }

    @Provides @Singleton
    fun mongoClient(mongoDB: EmbeddedMongoDB): MongoClient {
        return mongoDB.client()
    }

}