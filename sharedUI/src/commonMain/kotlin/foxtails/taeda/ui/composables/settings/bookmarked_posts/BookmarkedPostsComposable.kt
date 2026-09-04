package foxtails.taeda.ui.composables.settings.bookmarked_posts

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.timelines.parametric_timeline_screens.ParametricTimelineViewModel
import foxtails.taeda.ui.composables.timelines.parametric_timeline_screens.TimelineScreen
import org.jetbrains.compose.resources.stringResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.bookmarked_posts

@Composable
fun BookmarkedPostsComposable(
    navController: NavController,
    viewModel: ParametricTimelineViewModel = injectViewModel(key = "bookmarked-posts") {
        parametricTimelineViewModel.apply { init(ParametricTimelineViewModel.FetchType.BOOKMARKED_POSTS) }
    }
) {
    TimelineScreen(
        title = stringResource(Res.string.bookmarked_posts),
        navController = navController,
        viewModel = viewModel,
        isFirstItemLarge = true
    )
}