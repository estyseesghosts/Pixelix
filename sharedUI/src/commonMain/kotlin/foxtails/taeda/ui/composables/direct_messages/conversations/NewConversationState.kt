package foxtails.taeda.ui.composables.direct_messages.conversations

import foxtails.taeda.domain.model.Account

data class NewConversationState(
    val isLoading: Boolean = false,
    val error: String = "",
    val suggestions: List<Account> = emptyList()
)
