package com.daniebeler.pfpixelix.domain.repository.sharkey

import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyMiAuthCheckRequest
import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyMiAuthCheckResponse
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface SharkeyAuthApi {
    companion object {
        fun createSharkeyAuthApi(baseUrl: Url, json: Json): SharkeyAuthApi {
            val client = HttpClient {
                expectSuccess = true
                install(ContentNegotiation) { json(json) }
            }
            return Ktorfit.Builder()
                .httpClient(client)
                .baseUrl(baseUrl.toString())
                .build()
                .createSharkeyAuthApi()
        }
    }

    @Headers("Content-Type: application/json")
    @POST("api/miauth/{session}/check")
    suspend fun check(
        @Path("session") session: String,
        @Body request: SharkeyMiAuthCheckRequest
    ): SharkeyMiAuthCheckResponse
}