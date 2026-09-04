package foxtails.taeda.ui.composables.explore.trending.cameras

import foxtails.taeda.domain.model.Camera
import foxtails.taeda.domain.service.capabilities.Capabilities
import foxtails.taeda.domain.service.general.ExploreService
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.general.TimelineService
import foxtails.taeda.ui.composables.explore.trending.BasePagePaginatedViewModel
import kotlinx.coroutines.flow.StateFlow
import me.tatarka.inject.annotations.Inject

class CamerasViewModel @Inject constructor(
    private val exploreService: ExploreService,
    val timelineService: TimelineService,
    session: Session
) : BasePagePaginatedViewModel<Camera>(fetcher = { page -> exploreService.getCameras(page) }) {

    val capabilities: StateFlow<Capabilities> = session.capabilities
}