package foxtails.taeda.domain.service.pixelfed

import foxtails.taeda.domain.model.Notification
import foxtails.taeda.domain.model.NotificationType
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.general.NotificationService
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.service.utils.loadPaginatedListResources
import foxtails.taeda.domain.service.utils.loadResource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class PixelfedNotificationService(
    private val api: PixelfedApi
) : NotificationService {
    override fun getNotifications(maxNotificationId: String?) = loadPaginatedListResources {
        if (maxNotificationId == null) {
            coroutineScope {
                val notificationsDeferred = async {api.getNotifications()}
                val followRequestsDeferred = async {api.getFollowRequests()}

                val notifications = notificationsDeferred.await()
                val followRequests = followRequestsDeferred.await()

                val notificationsList = notifications.map { it.toDomain() }.toMutableList()
                val followRequestsNotifications = followRequests.map {
                    Notification(
                        account = it.toDomain(),
                        createdAt = "",
                        id = it.id,
                        post = null,
                        type = NotificationType.FOLLOW_REQUEST
                    )
                }
                notificationsList.addAll(0, followRequestsNotifications)
                notificationsList
            }
        } else {
            api.getNotifications(maxNotificationId).map { it.toDomain() }
        }
    }

    override fun getUnreadCount(): Flow<Resource<Int>> = loadResource {
        0
    }

    override fun markNotifications(notificationId: String): Flow<Resource<Unit>> = loadResource {
    }
}