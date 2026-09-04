package foxtails.taeda.ui.composables.settings.preferences.prefs.prefs

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import foxtails.taeda.ui.composables.settings.preferences.prefs.basic.SettingPref
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.more_settings
import foxtails.taeda.app.generated.resources.open
import foxtails.taeda.app.generated.resources.settings

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MoreSettingsPref(openUrl: () -> Unit) {
    SettingPref(
        icon = Res.drawable.settings,
        title = stringResource(Res.string.more_settings),
        trailingContent = {
            Icon(
                imageVector = vectorResource(Res.drawable.open), contentDescription = null
            )
        },
        shapes = ListItemDefaults.segmentedShapes(index = 1, count = 4),
        onClick = openUrl
    )
}

@Preview
@Composable
private fun MoreSettingsPrefPreview() {
    MoreSettingsPref(openUrl = {
        Logger.v("URL opened: url")
    })
}
