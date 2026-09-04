package foxtails.taeda.ui.composables.explore.trending.films

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.explore.trending.PagePaginatedListScreen
import foxtails.taeda.ui.composables.explore.trending.trending_hashtags.ExploreGridElement
import foxtails.taeda.ui.navigation.Destination
import foxtails.taeda.utils.StringFormat
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.no_films
import foxtails.taeda.app.generated.resources.posts

@Composable
fun FilmsComposable(
    navController: NavController,
    viewModel: FilmsViewModel = injectViewModel(key = "films-key") { filmsViewModel }
) {
    PagePaginatedListScreen(
        state = viewModel.pagePaginatedState,
        onRefresh = { viewModel.getItems(true) },
        onLoadMore = { viewModel.getItemsPaginated() },
        emptyMessage = stringResource(Res.string.no_films),
        itemKey = { it.id }
    ) { film ->
        ExploreGridElement(
            keyId = film.name,
            title = film.name,
            subtitle = "${StringFormat.groupDigits(film.amount)} ${pluralStringResource(Res.plurals.posts, film.amount)}",
            onClick = { navController.navigate(Destination.FilmTimeline(film.name)) },
            fetcher = { viewModel.timelineService.getFilmTimeline(it, limit = 39) },
            navController = navController
        )
    }
}