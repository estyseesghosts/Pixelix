package foxtails.taeda.ui.composables.timelines.global_timeline

import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import foxtails.taeda.di.LocalAppComponent
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.timelines.TimelineHelpCard
import foxtails.taeda.ui.composables.widgets.InfinitePostsList
import org.jetbrains.compose.resources.stringResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.global
import foxtails.taeda.app.generated.resources.global_timeline_explained

@Composable
fun GlobalTimelineComposable(
    pagerState: PagerState,
    tabIndex: Int,
    navController: NavController,
    viewModel: GlobalTimelineViewModel = injectViewModel(key = "global-timeline-key") { globalTimelineViewModel }
) {

    val staggeredGridState = rememberLazyStaggeredGridState()

    val appComponent = LocalAppComponent.current
    LaunchedEffect(Unit) {
        appComponent.backToTopTrigger.event.collect {
            if (pagerState.currentPage == tabIndex) {
                staggeredGridState.animateScrollToItem(0, 0)
                viewModel.refresh()
            }
        }
    }

    InfinitePostsList(
        items = viewModel.timelineState.posts,
        contentPaddingTop = 30.dp,
        isLoading = viewModel.timelineState.isLoading,
        isRefreshing = viewModel.timelineState.isRefreshing,
        error = viewModel.timelineState.error,
        view = viewModel.view,
        changeView = { viewModel.changeView(it) },
        endReached = false,
        navController = navController,
        getItemsPaginated = { viewModel.getItemsPaginated() },
        onRefresh = {
            viewModel.refresh()
        },
        staggeredGridState = staggeredGridState,
        itemGetsDeleted = { postId -> viewModel.postGetsDeleted(postId) },
        postGetsUpdated = { post -> viewModel.postGetsUpdated(post) },
        before = if (!viewModel.showTimelineHelp) {
            null
        } else {
            {
                TimelineHelpCard(
                    title = stringResource(Res.string.global),
                    description = stringResource(Res.string.global_timeline_explained),
                    onDiscard = {
                        viewModel.discardHelp()
                    })
            }
        })
}