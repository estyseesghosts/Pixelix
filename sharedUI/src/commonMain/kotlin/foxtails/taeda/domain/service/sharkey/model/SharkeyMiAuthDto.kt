package foxtails.taeda.domain.service.sharkey.model

import foxtails.taeda.domain.model.Credentials
import foxtails.taeda.domain.service.general.BackendType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharkeyMiAuthCheckRequest(
    val placeholder: String? = null
)

@Serializable
data class SharkeyMiAuthCheckResponse(
    val ok: Boolean = false,
    val token: String? = null,
    val user: SharkeyMiAuthUser? = null
)

@Serializable
data class SharkeyMiAuthUser(
    val id: String? = null,
    val username: String? = null,
    val name: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)

internal fun SharkeyMiAuthCheckResponse.toCredentials(serverUrl: String, createdAt: String): Credentials {
    require(ok) { "Sharkey rejected the MiAuth session." }
    val verifiedUser = requireNotNull(user) { "Sharkey did not return an authenticated user." }
    val accountId = verifiedUser.id?.takeIf { it.isNotBlank() }
        ?: error("Sharkey did not return an account ID.")
    val username = verifiedUser.username?.takeIf { it.isNotBlank() }
        ?: error("Sharkey did not return a username.")
    val accessToken = token?.takeIf { it.isNotBlank() }
        ?: error("Sharkey did not return an access token.")

    return Credentials(
        accountId = accountId,
        username = username,
        displayName = verifiedUser.name?.takeIf { it.isNotBlank() } ?: username,
        avatar = verifiedUser.avatarUrl.orEmpty(),
        serverUrl = serverUrl,
        token = accessToken,
        refreshToken = "",
        clientId = "",
        clientSecret = "",
        createdAt = createdAt,
        backendType = BackendType.SHARKEY
    )
}