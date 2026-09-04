package foxtails.taeda.ui.composables.explore.trending.trending_accounts

import foxtails.taeda.domain.model.Relationship

data class AccountRelationshipsState(
    val isLoading: Boolean = false,
    val accountRelationships: List<Relationship> = emptyList(),
    val error: String = ""
)
