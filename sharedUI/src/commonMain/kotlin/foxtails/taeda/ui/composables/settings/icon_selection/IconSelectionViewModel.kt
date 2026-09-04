package foxtails.taeda.ui.composables.settings.icon_selection

import androidx.lifecycle.ViewModel
import foxtails.taeda.domain.service.general.AppIconService
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.DrawableResource

@Inject
class IconSelectionViewModel(
    val appIconService: AppIconService
) : ViewModel() {
    val icons = appIconService.icons
    val selectedIcon = appIconService.currentIcon

    fun changeIcon(icon: DrawableResource) {
        appIconService.selectIcon(icon)
    }
}
