package foxtails.taeda.ui.composables.post.reply

import foxtails.taeda.domain.service.general.ReplyNode

data class RepliesState(
    val isLoading: Boolean = false,
    val replies: List<ReplyNode> = emptyList(),
    val error: String = ""
)
