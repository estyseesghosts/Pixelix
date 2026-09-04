package foxtails.taeda.ui.composables.notifications

import foxtails.taeda.domain.model.Relationship

data class FollowRequestState (
    val isLoading: Boolean = false,
    val error: String? = null,
    val relationship: Relationship? = null,
    val isAccepting: Boolean = true
)