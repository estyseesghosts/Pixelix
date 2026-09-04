package foxtails.taeda.ui.composables.post.reply

import foxtails.taeda.domain.model.Post

data class OwnReplyState(
    val isLoading: Boolean = false,
    val reply: Post? = null,
    val error: String = ""
)
