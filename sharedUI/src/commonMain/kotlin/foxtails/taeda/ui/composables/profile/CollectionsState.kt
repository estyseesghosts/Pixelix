package foxtails.taeda.ui.composables.profile

import foxtails.taeda.domain.model.Collection


data class CollectionsState(
    val isLoading: Boolean = false,
    val collections: List<Collection> = emptyList(),
    val endReached: Boolean = false,
    val error: String = ""
)
