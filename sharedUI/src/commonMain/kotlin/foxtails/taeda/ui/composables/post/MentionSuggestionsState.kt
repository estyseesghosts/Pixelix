package foxtails.taeda.ui.composables.post

import foxtails.taeda.domain.model.Account

data class SuggestionsState(
    val isLoading: Boolean = false,
    /**
     * suggestions: Pair of text, imageUrl
     */
    val suggestions: List<Pair<String, String?>> = emptyList(),
    val error: String = ""
)
