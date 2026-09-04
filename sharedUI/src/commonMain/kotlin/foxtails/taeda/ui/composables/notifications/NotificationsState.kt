package foxtails.taeda.ui.composables.notifications

import foxtails.taeda.domain.model.Notification

data class NotificationsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val error: String = "",
    val nextId: String? = null
)