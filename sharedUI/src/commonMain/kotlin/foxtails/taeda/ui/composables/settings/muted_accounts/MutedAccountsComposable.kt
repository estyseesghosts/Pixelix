package foxtails.taeda.ui.composables.settings.muted_accounts

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.states.EmptyState
import foxtails.taeda.ui.composables.states.EmptyStateComposable
import foxtails.taeda.ui.composables.states.ErrorComposable
import foxtails.taeda.ui.composables.states.LoadingComposable
import foxtails.taeda.ui.composables.widgets.AccountListScreen
import foxtails.taeda.ui.composables.widgets.CustomPullToRefreshBox
import foxtails.taeda.ui.composables.widgets.ScreenScaffold
import org.jetbrains.compose.resources.stringResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.muted_accounts
import foxtails.taeda.app.generated.resources.no_muted_accounts

@Composable
fun MutedAccountsComposable(
    navController: NavController,
    viewModel: MutedAccountsViewModel = injectViewModel(key = "muted-accounts-key") { mutedAccountsViewModel }
) {
    ScreenScaffold(
        title = stringResource(Res.string.muted_accounts), navController = navController
    ) {
        CustomPullToRefreshBox(
            isRefreshing = viewModel.mutedAccountsState.isRefreshing,
            onRefresh = { viewModel.getMutedAccounts(true) },
            modifier = Modifier.fillMaxSize(),
            animatedBox = true
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(300.dp),
                contentPadding = PaddingValues(top = 24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(viewModel.mutedAccountsState.mutedAccounts, key = { it.id }) { account ->
                    CustomMutedAccountRow(
                        mutedAccount = account, navController = navController, viewModel = viewModel
                    )
                }
            }
            if (viewModel.mutedAccountsState.mutedAccounts.isEmpty()) {
                if (viewModel.mutedAccountsState.isLoading && !viewModel.mutedAccountsState.isRefreshing) LoadingComposable()
                if (viewModel.mutedAccountsState.error.isNotEmpty()) ErrorComposable(
                    message = viewModel.mutedAccountsState.error,
                    modifier = Modifier.fillMaxSize().padding(36.dp, 20.dp)
                )
                if (!viewModel.mutedAccountsState.isLoading && viewModel.mutedAccountsState.error.isEmpty()) {
                    EmptyStateComposable(EmptyState(heading = stringResource(Res.string.no_muted_accounts)))
                }
            }
        }
    }
}
