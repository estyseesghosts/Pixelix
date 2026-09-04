package foxtails.taeda.ui.composables.settings.preferences.prefs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import foxtails.taeda.domain.service.capabilities.Capabilities
import foxtails.taeda.domain.service.general.AuthService
import foxtails.taeda.domain.service.general.AppIconService
import foxtails.taeda.domain.service.general.BackendType
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.platform.Platform
import foxtails.taeda.domain.service.suggestions.HashtagMentionsSuggestionsManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class PreferencesViewModel(
    private val authService: AuthService,
    private val platform: Platform,
    val suggestionsManager: HashtagMentionsSuggestionsManager,
    appIconService: AppIconService,
    session: Session
) : ViewModel() {
    val capabilities: StateFlow<Capabilities> = session.capabilities
    val backendType: BackendType = session.backendType.value
    val appIcon = appIconService.currentIcon
    val versionName = platform.getAppVersion()

    fun logout() {
        viewModelScope.launch {
            authService.deleteSession()
        }
    }

    fun openMoreSettingsPage() {
        authService.getCurrentSession()?.let {
            platform.openUrl(it.serverUrl + backendType.moreSettingsPath())
        }
    }

    fun openRepostSettings() {
        authService.getCurrentSession()?.let {
            platform.openUrl("${it.serverUrl}settings/timeline")
        }
    }

    fun openDeleteAccountPage() {
        val customUrl = "account"

        authService.getCurrentSession()?.let {
            platform.openUrl(it.serverUrl + customUrl)
        }
    }
}

internal fun BackendType.moreSettingsPath() = when (this) {
    BackendType.MASTODON -> "settings/profile"
    BackendType.SHARKEY -> "settings/profile"
}