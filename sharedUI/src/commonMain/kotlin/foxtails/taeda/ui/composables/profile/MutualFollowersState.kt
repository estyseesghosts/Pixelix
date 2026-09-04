package foxtails.taeda.ui.composables.profile

import foxtails.taeda.domain.model.Account

data class MutualFollowersState(
    val isLoading: Boolean = false,
    val mutualFollowers: List<Account> = emptyList(),
    val error: String = ""
)
