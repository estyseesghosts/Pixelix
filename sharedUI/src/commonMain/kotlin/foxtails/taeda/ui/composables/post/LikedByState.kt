package foxtails.taeda.ui.composables.post

import foxtails.taeda.domain.model.Account

data class LikedByState(
    val isLoading: Boolean = false,
    val likedBy: List<Account> = emptyList(),
    val error: String = ""
)
