package foxtails.taeda.ui.composables.timelines.global_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import foxtails.taeda.domain.service.general.TimelineService
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.ui.composables.widgets.PaginatedPostsViewModel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class GlobalTimelineViewModel @Inject constructor(
    private val timelineService: TimelineService,
    private val userPreferences: UserPreferences
) : PaginatedPostsViewModel(userPreferences) {
    var showTimelineHelp by mutableStateOf(false)

    init {
        loadItems(false)
        viewModelScope.launch {
            userPreferences.showGlobalTimelineHelpFlow.collect {
                showTimelineHelp = it
            }
        }
    }

    override fun fetchPage(maxId: String?) = timelineService.getGlobalTimeline(maxId)

    fun discardHelp() {
        viewModelScope.launch {
            userPreferences.showGlobalTimelineHelp = false
        }
    }
}
