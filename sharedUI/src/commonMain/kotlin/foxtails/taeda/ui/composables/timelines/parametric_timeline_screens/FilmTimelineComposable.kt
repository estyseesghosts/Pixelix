package foxtails.taeda.ui.composables.timelines.parametric_timeline_screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel

@Composable
fun FilmTimelineComposable(
    navController: NavController,
    film: String,
    viewModel: ParametricTimelineViewModel = injectViewModel(key = "film-$film") {
        parametricTimelineViewModel.apply { init(ParametricTimelineViewModel.FetchType.FILM, film) }
    }
) {
    TimelineScreen(
        title = film,
        subtitle = "Film",
        navController = navController,
        viewModel = viewModel,
        isFirstItemLarge = true
    )
}