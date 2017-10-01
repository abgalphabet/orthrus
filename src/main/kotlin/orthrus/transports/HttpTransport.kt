package orthrus.transports

/**
 * Created by arthur on 14/7/2017.
 */
interface HttpTransport {
    fun get(uri: String) : String
    fun post(uri: String, payload: Any, basicAuth: String?): String
}