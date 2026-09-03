package com.daniebeler.pfpixelix.domain.service.pixelfed

import com.daniebeler.pfpixelix.domain.service.general.AuthInterceptor
import com.daniebeler.pfpixelix.domain.service.general.Session
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpHeaders

class PixelfedAuthInterceptor(
    private val session: Session
) : AuthInterceptor {
    override suspend fun Sender.intercept(request: HttpRequestBuilder): HttpClientCall {
        val token = session.credentials.value?.token
        if (token != null) {
            request.headers[HttpHeaders.Authorization] = "Bearer $token"
        }
        return execute(request)
    }
}