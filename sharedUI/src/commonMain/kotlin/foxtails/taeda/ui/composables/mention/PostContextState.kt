package foxtails.taeda.ui.composables.mention

import foxtails.taeda.domain.model.PostContext

data class PostContextState (
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val postContext: PostContext? = null,
    val error: String = ""
)