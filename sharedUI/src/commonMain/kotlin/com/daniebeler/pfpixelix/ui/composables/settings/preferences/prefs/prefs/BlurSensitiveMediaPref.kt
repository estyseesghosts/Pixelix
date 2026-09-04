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
import pixelix.app.generated.resources.blur

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BlurSensitiveMediaPref() {
    val prefs = LocalAppComponent.current.preferences
    val state = remember { mutableStateOf(prefs.blurSensitiveContent) }
    LaunchedEffect(state.value) {
        prefs.blurSensitiveContent = state.value
    }
    SwitchPref(
        icon = Res.drawable.blur,
        title = "Blur sensitive media",
        shapes = ListItemDefaults.segmentedShapes(index = 1, count = 7),
        state = state
    )
}