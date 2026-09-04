package foxtails.taeda.ui.composables.collection

import foxtails.taeda.domain.model.Collection

data class CollectionState(
    val isLoading: Boolean = false,
    val id: String? = null,
    val collection: Collection? = null,
    val error: String = ""
)