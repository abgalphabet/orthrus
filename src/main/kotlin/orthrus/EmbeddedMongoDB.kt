package orthrus

import com.mongodb.MongoClient
import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodProcess
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import java.net.ServerSocket


class EmbeddedMongoDB(private val host: String = "localhost", targetPort: Int? = null) : Startable, Stoppable, GracefullyShutdownService() {
    private val executable: MongodExecutable
    private lateinit var mongod: MongodProcess
    private val port: Int

    init {
        port = targetPort ?: port()
        val mongodConfig = MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(Net(host, port, Network.localhostIsIPv6()))
                .build()

        executable = starter.prepare(mongodConfig)
    }

    private fun port(): Int {
        var port : Int  = 0
        ServerSocket(0).use {
            it.reuseAddress = true
            port = it.localPort
        }

        return port
    }

    fun client() : MongoClient {
        return MongoClient(host, port)
    }

    companion object {
        private val starter = MongodStarter.getDefaultInstance()
    }

    override fun start() {
        mongod = executable.start()
    }

    override fun stop() {
        executable.stop()
    }


}