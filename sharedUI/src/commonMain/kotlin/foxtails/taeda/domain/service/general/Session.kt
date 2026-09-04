package foxtails.taeda.domain.service.general

import foxtails.taeda.di.AppSingleton
import foxtails.taeda.domain.model.Credentials
import foxtails.taeda.domain.service.capabilities.Capabilities
import foxtails.taeda.domain.service.capabilities.MastodonCapabilities
import foxtails.taeda.domain.service.capabilities.NoCapabilities
import foxtails.taeda.domain.service.capabilities.SharkeyCapabilities
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.Sender
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.Url
import io.ktor.http.set
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject

@Inject
@AppSingleton
class Session {
    private val credentialsState = MutableStateFlow<Credentials?>(null)
    val credentials: StateFlow<Credentials?> = credentialsState.asStateFlow()

    private val backendTypeState = MutableStateFlow(BackendType.MASTODON)
    val backendType: StateFlow<BackendType> = backendTypeState.asStateFlow()

    private val capabilitiesSate = MutableStateFlow(NoCapabilities)
    val capabilities: StateFlow<Capabilities> = capabilitiesSate.asStateFlow()

    fun setCredentials(credentials: Credentials?) {
        credentialsState.value = credentials
        if (credentials != null) {
            setBackendType(credentials.backendType)
        }
    }

    fun setBackendType(backendType: BackendType) {
        backendTypeState.value = backendType
        capabilitiesSate.value = backendType.toCapabilities()
    }
}

enum class BackendType {
    MASTODON,
    SHARKEY
}

fun BackendType.toCapabilities(): Capabilities = when (this) {
    BackendType.MASTODON -> MastodonCapabilities
    BackendType.SHARKEY  -> SharkeyCapabilities
}