package foxtails.taeda.ui.composables.explore.trending.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import foxtails.taeda.domain.service.capabilities.Capabilities
import foxtails.taeda.domain.service.general.ExploreService
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.general.TimelineService
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.ui.composables.explore.trending.TrendingRange
import foxtails.taeda.ui.composables.explore.trending.trending_hashtags.TrendingHashtagsState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class CategoriesViewModel @Inject constructor(
    private val exploreService: ExploreService,
    val timelineService: TimelineService,
    session: Session
) : ViewModel() {
    val capabilities: StateFlow<Capabilities> = session.capabilities

    var categoriesState by mutableStateOf(CategoriesState())

    init {
        getCategories()
    }

    fun getCategories(refreshing: Boolean = false) {
        if (!refreshing && categoriesState.categories.isNotEmpty()) return

        fetchCategories(isRefreshing = refreshing)
    }

    private fun fetchCategories(isRefreshing: Boolean) {
        exploreService.getCategories().onEach { result ->
            categoriesState = when (result) {
                is Resource.Success -> {
                    val newCategories = result.data

                    categoriesState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        categories = newCategories,
                        error = ""
                    )
                }

                is Resource.Error -> {
                    categoriesState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = result.message
                    )
                }

                is Resource.Loading -> {
                    categoriesState.copy(
                        isLoading = true,
                        isRefreshing = isRefreshing
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}