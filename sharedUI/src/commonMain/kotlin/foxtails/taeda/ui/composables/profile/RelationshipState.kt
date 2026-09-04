package foxtails.taeda.ui.composables.profile

import foxtails.taeda.domain.model.Relationship

data class RelationshipState(
    val isLoading: Boolean = false,
    val accountRelationship: Relationship? = null,
    val error: String = ""
)
