package foxtails.taeda.ui.composables.direct_messages.conversations

import foxtails.taeda.domain.model.Conversation

data class ConversationsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val endReached: Boolean = false,
    val conversations: List<Conversation> = emptyList(),
    val error: String = ""
)
