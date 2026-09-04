package foxtails.taeda.ui.composables.explore.trending.trending_hashtags

import foxtails.taeda.domain.model.Tag

data class TrendingHashtagsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val trendingHashtags: List<Tag> = emptyList(),
    val error: String = "",
    val nextId: String? = null,
    val endReached: Boolean = false
)
