package foxtails.taeda.domain.service.general

import foxtails.taeda.di.AppSingleton
import foxtails.taeda.domain.model.Notification
import foxtails.taeda.domain.model.PaginatedResponse
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.service.pixelfed.PixelfedWidgetService
import foxtails.taeda.domain.service.utils.Resource
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

interface WidgetService {
    fun getNotifications(maxNotificationId: String? = null): Flow<Resource<PaginatedResponse<Notification>>>

    fun getLatestImage(): Flow<Resource<Post>>
}

@Inject
@AppSingleton
class WidgetServiceDelegate(
    private val session: Session,
    private val pixelfed: PixelfedWidgetService,
) : WidgetService {

    private val current: WidgetService
        get() = when (session.backendType.value) {
            else -> pixelfed
        }

    override fun getNotifications(maxNotificationId: String?): Flow<Resource<PaginatedResponse<Notification>>> =
        current.getNotifications(maxNotificationId)

    override fun getLatestImage(): Flow<Resource<Post>> = current.getLatestImage()

}