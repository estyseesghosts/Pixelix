package foxtails.taeda.widget


import androidx.compose.material3.darkColorScheme
import androidx.glance.material3.ColorProviders
import foxtails.taeda.ui.theme.primaryDark
import foxtails.taeda.ui.theme.secondaryDark
import foxtails.taeda.ui.theme.tertiaryDark

object WidgetColors {
    val colors = ColorProviders(
        light = DarkColorScheme,
        dark = DarkColorScheme
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = primaryDark,
    secondary = secondaryDark,
    tertiary = tertiaryDark,
)