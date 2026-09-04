package foxtails.taeda.ui.composables.explore.trending.lenses

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import foxtails.taeda.app.generated.resources.no_lenses
import foxtails.taeda.app.generated.resources.posts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LensesComposable(
    navController: NavController,
    viewModel: LensesViewModel = injectViewModel(key = "lenses-key") { lensesViewModel }
) {

    PagePaginatedListScreen(
        state = viewModel.pagePaginatedState,
        onRefresh = { viewModel.getItems(true) },
        onLoadMore = { viewModel.getItemsPaginated() },
        emptyMessage = stringResource(Res.string.no_lenses),
        itemKey = { it.id }
    ) { lens ->
        ExploreGridElement(
            keyId = lens.name,
            title = lens.name,
            subtitle = "${StringFormat.groupDigits(lens.amount)} ${pluralStringResource(Res.plurals.posts, lens.amount)}",
            onClick = { navController.navigate(Destination.LensTimeline(lens.name)) },
            fetcher = { viewModel.timelineService.getLensTimeline(it, limit = 39) },
            navController = navController
        )
    }
}