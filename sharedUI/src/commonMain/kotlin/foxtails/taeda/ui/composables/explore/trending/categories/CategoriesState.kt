package foxtails.taeda.ui.composables.explore.trending.categories

import foxtails.taeda.domain.model.Category

data class CategoriesState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val categories: List<Category> = emptyList(),
    val error: String = "",
)
