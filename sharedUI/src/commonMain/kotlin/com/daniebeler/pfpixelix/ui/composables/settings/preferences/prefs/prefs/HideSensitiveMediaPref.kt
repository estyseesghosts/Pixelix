package com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.prefs

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.daniebeler.pfpixelix.di.LocalAppComponent
import com.daniebeler.pfpixelix.ui.composables.settings.preferences.prefs.basic.SwitchPref
import pixelix.app.generated.resources.Res
import pixelix.app.generated.resources.eye_off

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HideSensitiveMediaPref() {
    val prefs = LocalAppComponent.current.preferences
    val state = remember { mutableStateOf(prefs.hideSensitiveMedia) }
    LaunchedEffect(state.value) {
        prefs.hideSensitiveMedia = state.value
    }
    SwitchPref(
        icon = Res.drawable.eye_off,
        title = "Hide sensitive media",
        shapes = ListItemDefaults.segmentedShapes(index = 0, count = 7),
        state = state
    )
}