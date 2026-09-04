package foxtails.taeda.ui.composables.explore.trending.trending_hashtags

import foxtails.taeda.domain.model.Post

data class ExploreGridPostsState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String = ""
)
