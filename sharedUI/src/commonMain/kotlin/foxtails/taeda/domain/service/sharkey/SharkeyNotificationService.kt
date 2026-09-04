package foxtails.taeda.domain.service.sharkey

import foxtails.taeda.domain.repository.sharkey.SharkeyApi
import foxtails.taeda.domain.service.general.NotificationService
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.sharkey.model.SharkeyRequest
import foxtails.taeda.domain.service.sharkey.model.toDomain
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.service.utils.loadPaginatedListResources
import foxtails.taeda.domain.service.utils.loadResource
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class SharkeyNotificationService(
    private val api: SharkeyApi,
    private val session: Session
) : NotificationService {
    override fun getNotifications(maxNotificationId: String?) = loadPaginatedListResources {
        api.getNotifications(request(untilId = maxNotificationId)).map { it.toDomain() }
    }

    override fun getUnreadCount(): Flow<Resource<Int>> = loadResource {
        api.getNotifications(request(limit = UNREAD_COUNT_LIMIT)).count { it.isRead == false }
    }

    override fun markNotifications(notificationId: String): Flow<Resource<Unit>> = loadResource {
        api.markNotificationAsRead(request(notificationId = notificationId))
    }

    private fun request(
        untilId: String? = null,
        limit: Int = DEFAULT_LIMIT,
        notificationId: String? = null
    ) = SharkeyRequest(
        i = requireNotNull(session.credentials.value?.token) { "No Sharkey session found." },
        untilId = untilId,
        limit = limit,
        markAsRead = false,
        notificationId = notificationId
    )

    private companion object {
        const val DEFAULT_LIMIT = 20
        const val UNREAD_COUNT_LIMIT = 100
    }
}