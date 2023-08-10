package data

import di.Network
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend inline fun <reified T> doGet(noinline block: HttpRequestBuilder.() -> Unit) : T {
   return Network.httpClient.get{
        block()
       parameter("with_origin_country", "CA")
       parameter("api_key", "1cfb9ad51213fd5acff399b2efda21dc")
    }.body()
}
