package foxtails.taeda.ui.composables.single_post

import foxtails.taeda.domain.model.Post

data class SinglePostState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val post: Post? = null,
    val error: String = ""
)
