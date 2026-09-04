package foxtails.taeda.ui.composables.followers

import foxtails.taeda.domain.model.Account

data class FollowersState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val followers: List<Account> = emptyList(),
    val cursor: String = "",
    val error: String = ""
)
