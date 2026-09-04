package foxtails.taeda.ui.composables.followers

import foxtails.taeda.domain.model.Account

data class FollowingState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val following: List<Account> = emptyList(),
    val cursor: String = "",
    val error: String = ""
)
