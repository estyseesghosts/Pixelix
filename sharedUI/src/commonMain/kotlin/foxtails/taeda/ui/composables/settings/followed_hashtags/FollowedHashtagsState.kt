package foxtails.taeda.ui.composables.settings.followed_hashtags

import foxtails.taeda.domain.model.Tag

data class FollowedHashtagsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val followedHashtags: List<Tag> = emptyList(),
    val error: String = ""
)
