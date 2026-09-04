package foxtails.taeda.ui.composables.direct_messages.chat

import foxtails.taeda.domain.model.Message

data class NewMessageState (
    val isLoading: Boolean = false,
    val message: Message? = null,
    val error: String = ""
)
