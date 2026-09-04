package foxtails.taeda.domain.service.sharkey

import foxtails.taeda.domain.service.general.AuthInterceptor
import foxtails.taeda.domain.service.general.BackendType
import foxtails.taeda.domain.service.general.Session
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url
import io.ktor.http.set

class SharkeyAuthInterceptor(
    private val session: Session
) : AuthInterceptor {
    override suspend fun Sender.intercept(request: HttpRequestBuilder): HttpClientCall {
        session.credentials.value
            ?.takeIf { it.backendType == BackendType.SHARKEY }
            ?.let { credentials -> request.url.set(host = Url(credentials.serverUrl).host) }
        return execute(request)
    }
}