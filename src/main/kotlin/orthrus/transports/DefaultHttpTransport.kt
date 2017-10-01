package orthrus.transports

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.apache.http.message.BasicHeader

open class DefaultHttpTransport @Inject
constructor(val mapper: ObjectMapper) : HttpTransport {

    override fun get(uri: String): String {

        return Request.Get(uri)
                .execute()
                .returnContent()
                .asString()
    }

    override fun post(uri: String, payload: Any, basicAuth: String?): String {
        return Request.Post(uri)
                .addHeader(BasicHeader("Authorization", basicAuth))
                .bodyString(mapper.writeValueAsString(payload), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString()
    }
}