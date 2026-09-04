package foxtails.taeda.domain.service.general

import foxtails.taeda.di.AppSingleton
import foxtails.taeda.domain.model.Credentials
import foxtails.taeda.domain.model.SessionStorage
import foxtails.taeda.domain.service.mastodon.MastodonAuthService
import foxtails.taeda.domain.service.sharkey.SharkeyAuthService
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

interface AuthService {
    companion object {
        const val clientName = "PLACEHOLDER"
        const val grantType = "authorization_code"
        val domainRegex: Regex =
            "^((\\*)|((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|((\\*\\.)?([a-zA-Z0-9-]+\\.){0,5}[a-zA-Z0-9-][a-zA-Z0-9-]+\\.[a-zA-Z]{2,63}?))\$".toRegex()
    }

    val activeUser: Flow<String?>

    suspend fun auth(host: String)

    suspend fun openSessionIfExist(key: String? = null)

    fun isValidHost(host: String): Boolean = domainRegex.matches(host)

    suspend fun deleteSession(keyParam: String? = null)

    suspend fun getAvailableSessions(): SessionStorage

    suspend fun updateSessionAvatar(accountId: String, avatarUrl: String)

    fun getCurrentSession(): Credentials?

    fun getServerUrl(host: String): Url {
        require(isValidHost(host)) { "The host is invalid '$host'" }
        return Url("https://$host/")
    }
}

@Inject
@AppSingleton
class AuthServiceDelegate(
    private val session: Session,
    private val mastodon: MastodonAuthService,
    private val sharkey: SharkeyAuthService
) : AuthService {

    private val current: AuthService
        get() = when (session.backendType.value) {
            BackendType.MASTODON -> mastodon
            BackendType.SHARKEY -> sharkey
        }
    override val activeUser: Flow<String?> = current.activeUser

    override suspend fun auth(host: String) = current.auth(host)

    override suspend fun openSessionIfExist(key: String?) = current.openSessionIfExist(key)

    override suspend fun deleteSession(keyParam: String?) = current.deleteSession(keyParam)

    override suspend fun getAvailableSessions(): SessionStorage = current.getAvailableSessions()

    override suspend fun updateSessionAvatar(accountId: String, avatarUrl: String) =
        current.updateSessionAvatar(accountId, avatarUrl)

    override fun getCurrentSession(): Credentials? = current.getCurrentSession()
}