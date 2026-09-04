package foxtails.taeda.domain.service.sharkey

import androidx.datastore.core.DataStore
import foxtails.taeda.di.AppSingleton
import foxtails.taeda.domain.model.Credentials
import foxtails.taeda.domain.model.SessionStorage
import foxtails.taeda.domain.repository.sharkey.SharkeyAuthApi.Companion.createSharkeyAuthApi
import foxtails.taeda.domain.service.general.AuthService
import foxtails.taeda.domain.service.general.BackendType
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.platform.Platform
import foxtails.taeda.domain.service.platform.redirectUrl
import foxtails.taeda.domain.service.search.SavedSearchesService
import foxtails.taeda.domain.service.sharkey.model.SharkeyMiAuthCheckRequest
import foxtails.taeda.domain.service.sharkey.model.toCredentials
import foxtails.taeda.ui.events.SystemUrlHandler
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlin.random.Random
import me.tatarka.inject.annotations.Inject

@Inject
@AppSingleton
class SharkeyAuthService(
    private val urlHandler: SystemUrlHandler,
    private val session: Session,
    private val sessionStorage: DataStore<SessionStorage>,
    private val savedSearchesService: SavedSearchesService,
    private val json: Json,
    private val platform: Platform
) : AuthService {
    override val activeUser: Flow<String?> = session.credentials.map { it?.accountId }

    override suspend fun auth(host: String) {
        val serverUrl = getServerUrl(host)
        val sessionId = newMiAuthSessionId()
        val authUrl = URLBuilder("${serverUrl}miauth/$sessionId").apply {
            parameters.append("name", AuthService.clientName)
            parameters.append("callback", platform.redirectUrl)
            parameters.append("permission", permissions.joinToString(","))
        }.build()

        urlHandler.isAuthInProgress = true
        platform.openUrl(authUrl.toString())
        val redirectString = urlHandler.redirects.first()
        platform.dismissBrowser()

        require(redirectString != "CANCELLED") { "User canceled the authentication flow." }
        val redirect = Url(redirectString)
        require(redirect.parameters["session"] == sessionId) { "Sharkey returned an invalid MiAuth session." }

        val credentials = createSharkeyAuthApi(serverUrl, json)
            .check(sessionId, SharkeyMiAuthCheckRequest())
            .toCredentials(serverUrl.toString(), "")
        updateSession(credentials)
    }

    private suspend fun updateSession(newCredentials: Credentials) {
        val targetKey = newCredentials.key()
        sessionStorage.updateData { data ->
            data.copy(
                sessions = data.sessions + (targetKey to newCredentials),
                activeKey = targetKey
            )
        }
        session.setCredentials(newCredentials)
    }

    override suspend fun openSessionIfExist(key: String?) {
        var resolvedCredentials: Credentials? = null
        sessionStorage.updateData { data ->
            val credentials = if (key == null) data.getActiveSession() else data.sessions[key]
            resolvedCredentials = credentials
            if (credentials != null) data.copy(activeKey = credentials.key()) else data
        }
        session.setCredentials(resolvedCredentials)
    }

    override suspend fun deleteSession(keyParam: String?) {
        sessionStorage.updateData { data ->
            val key = keyParam ?: data.activeKey
            val newSessions = data.sessions.filter { it.key != key }
            val activeKey = if (data.activeKey == key) newSessions.values.firstOrNull()?.key() else data.activeKey
            data.copy(sessions = newSessions, activeKey = activeKey)
        }
        if (keyParam == null) {
            savedSearchesService.clearSavedSearches()
            openSessionIfExist()
        }
    }

    override suspend fun getAvailableSessions(): SessionStorage = sessionStorage.data.first()

    override suspend fun updateSessionAvatar(accountId: String, avatarUrl: String) {
        sessionStorage.updateData { data ->
            data.copy(sessions = data.sessions.mapValues { (_, credentials) ->
                if (credentials.accountId == accountId) credentials.copy(avatar = avatarUrl) else credentials
            })
        }
        openSessionIfExist()
    }

    override fun getCurrentSession(): Credentials? = session.credentials.value

    private fun newMiAuthSessionId() = buildString {
        repeat(4) { append(Random.nextInt().toUInt().toString(16).padStart(8, '0')) }
    }

    private companion object {
        val permissions = listOf(
            "read:account",
            "read:blocks",
            "read:following",
            "read:messaging",
            "read:notifications",
            "read:reactions",
            "read:drive",
            "write:notes",
            "write:reactions",
            "write:messaging",
            "write:drive"
        )
    }
}