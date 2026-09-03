package com.daniebeler.pfpixelix.domain.repository.mastodon

import co.touchlab.kermit.Logger
import com.daniebeler.pfpixelix.domain.model.AuthData
import com.daniebeler.pfpixelix.domain.model.AuthTokenMastodon
import com.daniebeler.pfpixelix.domain.service.pixelfed.model.PixelfedAccountDto
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.http.Field
import de.jensklingenberg.ktorfit.http.FormUrlEncoded
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


interface MastodonAuthApi {
    companion object {
        fun createMastodonAuthApi(baseUrl: Url, json: Json): MastodonAuthApi {
            val httpClient = HttpClient {
                install(ContentNegotiation) { json(json) }
                install(Logging) {
                    logger = object : io.ktor.client.plugins.logging.Logger {
                        override fun log(message: String) {
                            Logger.v(tag = "Pixelix HttpAuth") {
                                message.lines().joinToString { "\n\t\t$it" }
                            }
                        }
                    }
                    level = LogLevel.INFO
                }
            }
            val ktorfit = Ktorfit.Builder()
                .httpClient(httpClient)
                .baseUrl(baseUrl.toString())
                .build()
            return ktorfit.createMastodonAuthApi()
        }
    }

    @FormUrlEncoded
    @POST("api/v1/apps")
    suspend fun getAuthData(
        @Field("client_name") clientName: String,
        @Field("redirect_uris") redirectUri: String,
        @Field("scopes") scopes: String
    ): AuthData

    @FormUrlEncoded
    @POST("oauth/token")
    suspend fun getToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String
    ): AuthTokenMastodon

    @GET("api/v1/accounts/verify_credentials")
    suspend fun verify(
        @Header("Authorization") token: String
    ): PixelfedAccountDto
}