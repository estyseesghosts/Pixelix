package foxtails.taeda.ui.composables.post_editor

import foxtails.taeda.domain.model.Post

data class PostSubmissionState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val error: String = ""
)
