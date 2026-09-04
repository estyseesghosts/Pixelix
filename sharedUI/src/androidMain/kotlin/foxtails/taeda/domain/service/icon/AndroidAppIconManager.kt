package foxtails.taeda.domain.service.icon

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import co.touchlab.kermit.Logger
import foxtails.taeda.domain.service.general.AppIconManager
import org.jetbrains.compose.resources.DrawableResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.app_icon_00
import foxtails.taeda.app.generated.resources.app_icon_01
import foxtails.taeda.app.generated.resources.app_icon_02
import foxtails.taeda.app.generated.resources.app_icon_03
import foxtails.taeda.app.generated.resources.app_icon_05
import foxtails.taeda.app.generated.resources.app_icon_06
import foxtails.taeda.app.generated.resources.app_icon_07
import foxtails.taeda.app.generated.resources.app_icon_08
import foxtails.taeda.app.generated.resources.app_icon_09

class AndroidAppIconManager(
    private val context: Context
) : AppIconManager {
    private val iconIds = mapOf(
        Res.drawable.app_icon_00 to "foxtails.taeda.Icon04",
        Res.drawable.app_icon_01 to "foxtails.taeda.Icon01",
        Res.drawable.app_icon_02 to "foxtails.taeda.Icon02",
        Res.drawable.app_icon_03 to "foxtails.taeda.Icon03",
        Res.drawable.app_icon_05 to "foxtails.taeda.Icon05",
        Res.drawable.app_icon_06 to "foxtails.taeda.Icon06",
        Res.drawable.app_icon_07 to "foxtails.taeda.Icon07",
        Res.drawable.app_icon_08 to "foxtails.taeda.Icon08",
        Res.drawable.app_icon_09 to "foxtails.taeda.Icon09",
    )

    override fun getCurrentIcon(): DrawableResource {
        for ((res, id) in iconIds.entries) {
            val i = context.packageManager.getComponentEnabledSetting(ComponentName(context, id))
            if (i == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                return res
            }
        }
        return Res.drawable.app_icon_02
    }

    override fun setCustomIcon(icon: DrawableResource) {
        try {
            val pm = context.packageManager
            for ((res, id) in iconIds.entries) {
                val state =
                    if (res == icon) PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    else PackageManager.COMPONENT_ENABLED_STATE_DISABLED

                pm.setComponentEnabledSetting(
                    ComponentName(context, id),
                    state,
                    PackageManager.DONT_KILL_APP
                )
            }
        } catch (e: Error) {
            Logger.e("enableCustomIcon", e)
        }
    }
}