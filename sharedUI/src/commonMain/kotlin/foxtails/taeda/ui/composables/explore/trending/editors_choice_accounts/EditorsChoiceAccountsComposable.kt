package foxtails.taeda.ui.composables.explore.trending.editors_choice_accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.explore.trending.trending_accounts.TrendingAccountElement
import foxtails.taeda.ui.composables.states.EmptyState
import foxtails.taeda.ui.composables.states.EmptyStateComposable
import foxtails.taeda.ui.composables.states.ErrorComposable
import foxtails.taeda.ui.composables.states.LoadingComposable
import foxtails.taeda.ui.composables.timelines.TimelineHelpCard
import foxtails.taeda.ui.composables.widgets.CustomPullToRefreshBox
import foxtails.taeda.ui.composables.widgets.InfiniteListHandler
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.datetime
import foxtails.taeda.app.generated.resources.editors_choice_accounts
import foxtails.taeda.app.generated.resources.editors_choice_accounts_explained
import foxtails.taeda.app.generated.resources.no_trending_profiles

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditorsChoiceAccountsComposable(
    navController: NavController,
    viewModel: EditorsChoiceAccountsViewModel = injectViewModel(key = "editors-choice-accounts-key") { editorsChoiceAccountsViewModel }
) {

    val calendarIcon = vectorResource(Res.drawable.datetime)
    val lazyListState = rememberLazyListState()

    CustomPullToRefreshBox(
        isRefreshing = viewModel.accountsState.isRefreshing,
        onRefresh = { viewModel.getAccountsState(true) },
        animatedBox = true
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            contentPadding = PaddingValues(top = 32.dp, bottom = 72.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            content = {
                if (viewModel.showHelp) {
                   item {
                       TimelineHelpCard(
                           title = stringResource(Res.string.editors_choice_accounts),
                           description = stringResource(Res.string.editors_choice_accounts_explained),
                           onDiscard = {
                               viewModel.discardHelp()
                           }
                       )
                   }
                }
                items(viewModel.accountsState.accounts, key = {
                    it.id
                }) {
                    TrendingAccountElement(
                        account = it, navController = navController
                    )
                }
            })
        if (viewModel.accountsState.accounts.isEmpty()) {
            if (viewModel.accountsState.isLoading && !viewModel.accountsState.isRefreshing) {
                LoadingComposable()
            }

            if (viewModel.accountsState.error.isNotEmpty()) {
                ErrorComposable(
                    message = viewModel.accountsState.error,
                    modifier = Modifier.fillMaxSize().padding(36.dp, 20.dp)
                )
            }

            if (!viewModel.accountsState.isLoading && viewModel.accountsState.error.isEmpty()) {
                EmptyStateComposable(EmptyState(heading = stringResource(Res.string.no_trending_profiles)))
            }
        }
    }

    InfiniteListHandler(
        lazyListState = lazyListState
    ) {
        viewModel.getAccountsPaginated()
    }
}