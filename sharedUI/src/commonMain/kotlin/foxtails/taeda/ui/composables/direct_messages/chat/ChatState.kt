package foxtails.taeda.ui.composables.direct_messages.chat

import foxtails.taeda.domain.model.Chat

data class ChatState(
    val isLoading: Boolean = false,
    val chat: Chat? = null,
    val endReached: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String = ""
)
