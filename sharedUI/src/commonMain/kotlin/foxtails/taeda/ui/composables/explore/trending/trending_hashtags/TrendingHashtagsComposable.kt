package foxtails.taeda.ui.composables.explore.trending.trending_hashtags

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.explore.trending.TrendingRange
import foxtails.taeda.ui.composables.states.EmptyState
import foxtails.taeda.ui.composables.states.EmptyStateComposable
import foxtails.taeda.ui.composables.states.ErrorComposable
import foxtails.taeda.ui.composables.states.LoadingComposable
import foxtails.taeda.ui.composables.widgets.CustomPullToRefreshBox
import foxtails.taeda.ui.composables.widgets.InfiniteListHandler
import foxtails.taeda.ui.navigation.Destination
import foxtails.taeda.utils.StringFormat
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.datetime
import foxtails.taeda.app.generated.resources.no_trending_hashtags
import foxtails.taeda.app.generated.resources.posts

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TrendingHashtagsComposable(
    navController: NavController,
    viewModel: TrendingHashtagsViewModel = injectViewModel(key = "trending-hashtags-key") { trendingHashtagsViewModel }
) {

    val calendarIcon = vectorResource(Res.drawable.datetime)
    val lazyListState = rememberLazyListState()

    CustomPullToRefreshBox(
        isRefreshing = viewModel.trendingHashtagsState.isRefreshing,
        onRefresh = { viewModel.getTrendingHashtags(true) },
        animatedBox = true
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            contentPadding = PaddingValues(top = 32.dp, bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            content = {
                if (viewModel.capabilities.value.trending.supportsMultipleHashtagTimeRanges) {
                    item {
                        ButtonGroup(overflowIndicator = { Text("Daily") }) {
                            toggleableItem(
                                weight = 1f,
                                checked = viewModel.timeRange == TrendingRange.DAILY,
                                onCheckedChange = { viewModel.changeTimeRange(TrendingRange.DAILY) },
                                label = "Daily",
                                icon = {
                                    if (viewModel.timeRange == TrendingRange.DAILY) {
                                        Icon(imageVector = calendarIcon, contentDescription = "")
                                    }
                                })

                            toggleableItem(
                                weight = 1f,
                                checked = viewModel.timeRange == TrendingRange.MONTHLY,
                                onCheckedChange = { viewModel.changeTimeRange(TrendingRange.MONTHLY) },
                                label = "Monthly",
                                icon = {
                                    if (viewModel.timeRange == TrendingRange.MONTHLY) {
                                        Icon(imageVector = calendarIcon, contentDescription = "")
                                    }
                                })

                            toggleableItem(
                                weight = 1f,
                                checked = viewModel.timeRange == TrendingRange.YEARLY,
                                onCheckedChange = { viewModel.changeTimeRange(TrendingRange.YEARLY) },
                                label = "Yearly",
                                icon = {
                                    if (viewModel.timeRange == TrendingRange.YEARLY) {
                                        Icon(imageVector = calendarIcon, contentDescription = "")
                                    }
                                })
                        }
                    }
                }

                items(viewModel.trendingHashtagsState.trendingHashtags, key = {
                    if (!it.hashtag.isNullOrEmpty()) {
                        it.hashtag
                    } else {
                        it.name
                    }
                }) {
                    ExploreGridElement(
                        keyId = it.name,
                        title = "#${it.name}",
                        subtitle = it.postsCount?.let {
                            "${StringFormat.groupDigits(it)} ${pluralStringResource(Res.plurals.posts, it)}"
                        },
                        onClick = {
                            navController.navigate(Destination.HashtagTimeline(it.name))
                        },
                        fetcher = { tag -> viewModel.timelineService.getHashtagTimeline(tag, limit = 39) },
                        navController = navController
                    )
                }

                if (viewModel.trendingHashtagsState.isLoading && viewModel.trendingHashtagsState.trendingHashtags.isNotEmpty()) {
                    item {
                        LoadingComposable()
                    }
                }
            })

        if (viewModel.trendingHashtagsState.trendingHashtags.isEmpty()) {
            if (viewModel.trendingHashtagsState.isLoading && !viewModel.trendingHashtagsState.isRefreshing) {
                LoadingComposable()
            }

            if (viewModel.trendingHashtagsState.error.isNotEmpty()) {
                ErrorComposable(
                    message = viewModel.trendingHashtagsState.error,
                    modifier = Modifier.fillMaxSize().padding(36.dp, 20.dp)
                )
            }

            if (!viewModel.trendingHashtagsState.isLoading && viewModel.trendingHashtagsState.error.isEmpty()) {
                EmptyStateComposable(EmptyState(heading = stringResource(Res.string.no_trending_hashtags)))
            }
        }
    }

    InfiniteListHandler(
        lazyListState = lazyListState
    ) {
        viewModel.getTrendingHashtagsPaginated()
    }
}