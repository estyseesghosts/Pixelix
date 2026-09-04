package foxtails.taeda.ui.composables.explore

import foxtails.taeda.domain.model.Search

data class SearchState(
    val isLoading: Boolean = false,
    val searchResult: Search? = null,
    val error: String = ""
)
