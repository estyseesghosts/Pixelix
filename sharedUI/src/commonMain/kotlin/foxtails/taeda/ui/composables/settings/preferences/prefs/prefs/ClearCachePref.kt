package foxtails.taeda.ui.composables.settings.preferences.prefs.prefs

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.settings.preferences.prefs.basic.SettingPref
import org.jetbrains.compose.resources.stringResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.clear_cache
import foxtails.taeda.app.generated.resources.save

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ClearCachePref(drawerState: DrawerState) {
    val viewModel = injectViewModel("ClearCacheViewModel") { clearCacheViewModel }
    val cacheSize = viewModel.cacheSize.collectAsStateWithLifecycle("")

    LaunchedEffect(drawerState.isOpen) {
        viewModel.refresh()
    }

    SettingPref(
        icon = Res.drawable.save,
        title = stringResource(Res.string.clear_cache),
        desc = cacheSize.value,
        shapes = ListItemDefaults.segmentedShapes(index = 0, count = 4),
        onClick = { viewModel.cleanCache() }
    )
}
