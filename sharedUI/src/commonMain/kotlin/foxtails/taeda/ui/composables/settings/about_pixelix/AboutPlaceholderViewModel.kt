package foxtails.taeda.ui.composables.settings.about_pixelix

import androidx.lifecycle.ViewModel
import foxtails.taeda.domain.service.general.AppIconService
import foxtails.taeda.domain.service.platform.Platform
import me.tatarka.inject.annotations.Inject

class AboutPlaceholderViewModel @Inject constructor(
    private val platform: Platform,
    private val appIconService: AppIconService
) : ViewModel() {
    val versionName = platform.getAppVersion()
    val appIcon = appIconService.currentIcon

    fun rateApp() {
        platform.openUrl(
             "https://play.google.com/store/apps/details?id=foxtails.taeda"
        )
    }

    fun openUrl(url: String) {
        platform.openUrl(url)
    }
}