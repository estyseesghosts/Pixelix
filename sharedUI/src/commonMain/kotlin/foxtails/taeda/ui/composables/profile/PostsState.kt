package foxtails.taeda.ui.composables.profile

import foxtails.taeda.domain.model.Post

data class PostsState(
    val isLoading: Boolean = false,
    val refreshing: Boolean = false,
    val endReached: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String = "",
    val nextId: String? = null
)
