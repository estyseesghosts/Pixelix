package foxtails.taeda.ui.composables.post_editor

import foxtails.taeda.domain.model.Category

data class CategoriesState(
    var isLoading: Boolean = false,
    var error: String = "",
    var selectedCategory: Category? = null,
    var categories: List<Category> = emptyList()
)
