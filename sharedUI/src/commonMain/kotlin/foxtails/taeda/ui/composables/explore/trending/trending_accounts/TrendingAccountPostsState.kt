package foxtails.taeda.ui.composables.explore.trending.trending_accounts

import foxtails.taeda.domain.model.Post

data class TrendingAccountPostsState (
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String = ""
)