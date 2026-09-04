package foxtails.taeda.domain.service.icon

import foxtails.taeda.domain.service.general.AppIconManager
import org.jetbrains.compose.resources.DrawableResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.app_icon_02

class DesktopAppIconManager : AppIconManager {

    override fun getCurrentIcon(): DrawableResource {
        return Res.drawable.app_icon_02
    }

    override fun setCustomIcon(icon: DrawableResource) {}
}