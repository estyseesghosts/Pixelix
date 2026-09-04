package foxtails.taeda.ui.composables.collection

import foxtails.taeda.domain.model.Post

data class CollectionPostsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String = ""
)