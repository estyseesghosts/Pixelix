package foxtails.taeda.domain.service.platform

import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.utils.KmpContext
import foxtails.taeda.utils.KmpUri
import io.github.vinceglb.filekit.PlatformFile
import me.tatarka.inject.annotations.Inject

@Inject
expect class Platform(
    context: KmpContext,
    prefs: UserPreferences
) {
    fun toSafeUri(platformFile: PlatformFile): KmpUri
    fun openUrl(url: String)
    fun dismissBrowser()
    fun shareText(text: String)
    fun getAppVersion(): String
    fun pinWidget()
}

internal expect val Platform.redirectUrl: String
