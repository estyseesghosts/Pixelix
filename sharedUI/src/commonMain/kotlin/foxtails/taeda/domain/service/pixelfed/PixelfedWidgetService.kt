package foxtails.taeda.domain.service.pixelfed

import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.general.WidgetService
import foxtails.taeda.domain.service.pixelfed.model.toDomain
import foxtails.taeda.domain.service.utils.loadListResources
import foxtails.taeda.domain.service.utils.loadPaginatedListResources
import foxtails.taeda.domain.service.utils.loadResource
import me.tatarka.inject.annotations.Inject

@Inject
class PixelfedWidgetService(
    private val api: PixelfedApi
) : WidgetService {
    override fun getNotifications(maxNotificationId: String?) = loadPaginatedListResources {
        api.getNotifications(maxNotificationId).map { it.toDomain() }
    }

    override fun getLatestImage() = loadResource {
        api.getHomeTimeline(limit = 5).first { post -> post.mediaAttachments[0].type == "image" }.toDomain()
    }
}