package foxtails.taeda.ui.composables.settings.liked_posts

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.timelines.parametric_timeline_screens.ParametricTimelineViewModel
import foxtails.taeda.ui.composables.timelines.parametric_timeline_screens.TimelineScreen
import org.jetbrains.compose.resources.stringResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.liked_posts

@Composable
fun LikedPostsComposable(
    navController: NavController,
    viewModel: ParametricTimelineViewModel = injectViewModel(key = "liked-posts") {
        parametricTimelineViewModel.apply { init(ParametricTimelineViewModel.FetchType.LIKED_POSTS) }
    }
) {
    TimelineScreen(
        title = stringResource(Res.string.liked_posts),
        navController = navController,
        viewModel = viewModel,
        isFirstItemLarge = true
    )
}