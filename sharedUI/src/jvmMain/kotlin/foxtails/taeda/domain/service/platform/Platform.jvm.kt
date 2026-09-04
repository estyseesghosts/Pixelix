package foxtails.taeda.domain.service.platform

import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.utils.KmpContext
import foxtails.taeda.utils.KmpUri
import foxtails.taeda.utils.toKmpUri
import io.github.vinceglb.filekit.PlatformFile
import me.tatarka.inject.annotations.Inject
import java.awt.Desktop
import java.net.URI

@Inject
actual class Platform actual constructor(
    private val context: KmpContext,
    private val prefs: UserPreferences
) {
    actual fun openUrl(url: String) {
        val os = System.getProperty("os.name").lowercase()

        if (os.contains("linux")) {
            try {
                ProcessBuilder("xdg-open", url).start()
            } catch (e: Throwable) {
                println("Flatpak: Failed to open URL via xdg-open: ${e.message}")
                runCatching {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(URI(url))
                    }
                }
            }
        } else {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(URI(url))
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    actual fun dismissBrowser() {}

    actual fun shareText(text: String) {}

    actual fun getAppVersion(): String {
        return "1.0.0"
    }

    actual fun pinWidget() {}
    actual fun toSafeUri(platformFile: PlatformFile): KmpUri {
        return platformFile.toKmpUri()
    }
}
