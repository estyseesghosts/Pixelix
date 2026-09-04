package foxtails.taeda.ui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.WindowInsetsControllerCompat
import co.touchlab.kermit.Logger
import foxtails.taeda.MyApplication
import foxtails.taeda.di.LocalAppComponent
import foxtails.taeda.domain.model.AppThemeMode.AMOLED
import foxtails.taeda.domain.model.AppThemeMode.DARK

@Composable
actual fun generateColorScheme(
    nightModeValue: Int,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme {
    val context = LocalAppComponent.current.context
    return remember(
        nightModeValue, dynamicColor, lightScheme, darkScheme
    ) {
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when (nightModeValue) {
                AMOLED -> dynamicDarkColorScheme(context).toAmoled()
                DARK -> dynamicDarkColorScheme(context)
                else -> dynamicLightColorScheme(context)
            }
        } else {
            when (nightModeValue) {
                AMOLED -> darkScheme.toAmoled()
                DARK -> darkScheme
                else -> lightScheme
            }
        }
    }
}

actual fun applySystemNightMode(isDark: Boolean) {
    val activity = MyApplication.currentActivity?.get() ?: return
    val window = activity.window
    Logger.d { "applySystemNightMode isDark=$isDark" }
    WindowInsetsControllerCompat(window, window.decorView).apply {
        isAppearanceLightStatusBars = !isDark
        isAppearanceLightNavigationBars = !isDark
    }
}