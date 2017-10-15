package orthrus.conf

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import orthrus.web.handlers.AnnotatedHandler
import orthrus.web.handlers.ConfHandler
import orthrus.web.handlers.ProfileHandler

class MainModule : AbstractModule() {

    override fun configure() {
        bind(ConfHandler::class.java)
        bind(AnnotatedHandler::class.java)
        bind(ProfileHandler::class.java)
    }

    @Provides @Singleton
    fun provideConfig(): Config {
        return ConfigFactory.load("application.prod.conf")
    }

}