package foxtails.taeda.ui.composables.timelines.local_timeline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import foxtails.taeda.domain.service.general.TimelineService
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.ui.composables.widgets.PaginatedPostsViewModel
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class LocalTimelineViewModel @Inject constructor(
    private val timelineService: TimelineService,
    private val userPreferences: UserPreferences
) : PaginatedPostsViewModel(userPreferences) {
    var showTimelineHelp by mutableStateOf(false)

    init {
        loadItems(false)
        viewModelScope.launch {
            userPreferences.showLocalTimelineHelpFlow.collect {
                showTimelineHelp = it
            }
        }
    }

    override fun fetchPage(maxId: String?) = timelineService.getLocalTimeline(maxId)

    fun discardHelp() {
        viewModelScope.launch {
            userPreferences.showLocalTimelineHelp = false
        }
    }
}
