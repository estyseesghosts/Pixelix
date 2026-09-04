package foxtails.taeda.ui.composables.settings.preferences.prefs.prefs

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import foxtails.taeda.di.LocalAppComponent
import foxtails.taeda.ui.composables.settings.preferences.prefs.basic.SwitchPref
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.volume_mute

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuteVideosByDefaultPref() {
    val prefs = LocalAppComponent.current.preferences
    val state = remember { mutableStateOf(prefs.muteVideosByDefault) }
    LaunchedEffect(state.value) {
        prefs.muteVideosByDefault = state.value
    }
    SwitchPref(
        icon = Res.drawable.volume_mute,
        title = "Mute videos by default",
        shapes = ListItemDefaults.segmentedShapes(index = 6, count = 7),
        state = state
    )
}