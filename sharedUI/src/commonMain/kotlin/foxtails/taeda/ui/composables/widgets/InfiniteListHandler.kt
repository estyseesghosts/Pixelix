package foxtails.taeda.ui.composables.widgets

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

private fun shouldLoadMore(
    totalItems: Int,
    lastVisibleIndex: Int,
    buffer: Int,
    isLoading: Boolean,
    canLoadMore: Boolean
): Boolean =
    !isLoading && canLoadMore && totalItems != 0 && lastVisibleIndex + 1 > totalItems - buffer

@Composable
fun InfiniteListHandler(
    lazyListState: LazyListState,
    buffer: Int = 2,
    isLoading: Boolean = false,
    canLoadMore: Boolean = true,
    onLoadMore: () -> Unit
) {
    val shouldLoad by remember(buffer, isLoading, canLoadMore) {
        derivedStateOf {
            val layoutInfo = lazyListState.layoutInfo
            shouldLoadMore(
                totalItems = layoutInfo.totalItemsCount,
                lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0,
                buffer = buffer,
                isLoading = isLoading,
                canLoadMore = canLoadMore
            )
        }
    }

    if (shouldLoad) {
        LaunchedEffect(lazyListState.layoutInfo.totalItemsCount, isLoading, canLoadMore) {
            onLoadMore()
        }
    }
}

@Composable
fun InfiniteStaggeredGridHandler(
    lazyStaggeredGridState: LazyStaggeredGridState,
    itemCount: Int,
    buffer: Int = 2,
    isLoading: Boolean = false,
    canLoadMore: Boolean = true,
    onLoadMore: () -> Unit
) {
    val shouldLoad by remember(buffer, isLoading, canLoadMore) {
        derivedStateOf {
            val layoutInfo = lazyStaggeredGridState.layoutInfo
            shouldLoadMore(
                totalItems = layoutInfo.totalItemsCount,
                lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0,
                buffer = buffer,
                isLoading = isLoading,
                canLoadMore = canLoadMore
            )
        }
    }

    if (shouldLoad) {
        LaunchedEffect(
            lazyStaggeredGridState.layoutInfo.totalItemsCount,
            itemCount,
            isLoading,
            canLoadMore
        ) {
            onLoadMore()
        }
    }
}
