package foxtails.taeda.ui.composables.settings.preferences.prefs.prefs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import foxtails.taeda.di.LocalAppComponent
import foxtails.taeda.domain.model.AppThemeMode.AMOLED
import foxtails.taeda.domain.model.AppThemeMode.DARK
import foxtails.taeda.domain.model.AppThemeMode.FOLLOW_SYSTEM
import foxtails.taeda.domain.model.AppThemeMode.LIGHT
import foxtails.taeda.domain.service.platform.PlatformFeatures
import foxtails.taeda.ui.composables.settings.preferences.basic.ExpandOptionsPref
import foxtails.taeda.ui.composables.settings.preferences.prefs.basic.SwitchPref
import foxtails.taeda.ui.composables.settings.preferences.basic.ValueOption
import foxtails.taeda.ui.composables.settings.preferences.basic.imageVectorIconBlock
import foxtails.taeda.ui.composables.settings.preferences.basic.radioButtonBlock
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.amoled
import foxtails.taeda.app.generated.resources.amoled_theme
import foxtails.taeda.app.generated.resources.app_theme
import foxtails.taeda.app.generated.resources.theme
import foxtails.taeda.app.generated.resources.dark_theme
import foxtails.taeda.app.generated.resources.device_theme
import foxtails.taeda.app.generated.resources.light_theme
import foxtails.taeda.app.generated.resources.theme_dark
import foxtails.taeda.app.generated.resources.theme_light
import foxtails.taeda.app.generated.resources.theme_system
import foxtails.taeda.app.generated.resources.use_dynamic_colors

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ThemePref() {
    val pref = LocalAppComponent.current.preferences
    val appTheme by pref.appThemeModeFlow.collectAsState(pref.appThemeMode)
    val useDynamicColors = remember { mutableStateOf(pref.useDynamicColors) }

    val onOptionClick = { mode: Int ->
        pref.appThemeMode = mode
    }

    LaunchedEffect(useDynamicColors.value) {
        pref.useDynamicColors = useDynamicColors.value
    }

    ExpandOptionsPref(
        leadingIcon = Res.drawable.theme,
        title = stringResource(Res.string.app_theme),
        index = 1,
        count = 5
    ) {
        ValueOption(
            shapes = ListItemDefaults.segmentedShapes(index = 2, count = 9),
            leadingIcon = imageVectorIconBlock(
                imageVector = vectorResource(Res.drawable.device_theme),
                contentDescription = stringResource(Res.string.theme_system)
            ),
            title = stringResource(Res.string.theme_system),
            trailingContent = radioButtonBlock(appTheme == FOLLOW_SYSTEM),
            value = FOLLOW_SYSTEM,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shapes = ListItemDefaults.segmentedShapes(index = 2, count = 9),
            leadingIcon = imageVectorIconBlock(
                imageVector = vectorResource(Res.drawable.light_theme),
                contentDescription = stringResource(Res.string.theme_light)
            ),
            title = stringResource(Res.string.theme_light),
            trailingContent = radioButtonBlock(appTheme == LIGHT),
            value = LIGHT,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shapes = ListItemDefaults.segmentedShapes(index = 3, count = 9),
            leadingIcon = imageVectorIconBlock(
                imageVector = vectorResource(Res.drawable.dark_theme),
                contentDescription = stringResource(Res.string.theme_dark)
            ),
            title = stringResource(Res.string.theme_dark),
            trailingContent = radioButtonBlock(appTheme == DARK),
            value = DARK,
            onOptionClick = onOptionClick,
        )
        ValueOption(
            shapes = ListItemDefaults.segmentedShapes(index = 4, count = 9),
            leadingIcon = imageVectorIconBlock(
                imageVector = vectorResource(Res.drawable.amoled_theme),
                contentDescription = stringResource(Res.string.amoled)
            ),
            title = stringResource(Res.string.amoled),
            trailingContent = radioButtonBlock(appTheme == AMOLED),
            value = AMOLED,
            onOptionClick = onOptionClick,
        )

        if (!PlatformFeatures.supportsDynamicColors) {
            Spacer(modifier = Modifier.height(1.dp))

            CustomAccentColorPref()
        } else {
            SwitchPref(
                icon = Res.drawable.theme,
                title = stringResource(Res.string.use_dynamic_colors),
                shapes = ListItemDefaults.segmentedShapes(index = 5, count = 9),
                state = useDynamicColors,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            )
            AnimatedVisibility(visible = !useDynamicColors.value) {
                CustomAccentColorPref()
            }
        }
    }
}