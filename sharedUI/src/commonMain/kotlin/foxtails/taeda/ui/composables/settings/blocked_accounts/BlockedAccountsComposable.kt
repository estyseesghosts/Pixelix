package foxtails.taeda.ui.composables.settings.blocked_accounts

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import foxtails.taeda.di.injectViewModel
import foxtails.taeda.ui.composables.widgets.AccountListScreen
import org.jetbrains.compose.resources.stringResource
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.blocked_accounts
import foxtails.taeda.app.generated.resources.no_blocked_accounts

@Composable
fun BlockedAccountsComposable(
    navController: NavController,
    viewModel: BlockedAccountsViewModel = injectViewModel(key = "blocked-accounts-key") { blockedAccountsViewModel }
) {
    AccountListScreen(
        title = stringResource(Res.string.blocked_accounts),
        navController = navController,
        items = viewModel.blockedAccountsState.blockedAccounts,
        isLoading = viewModel.blockedAccountsState.isLoading,
        isRefreshing = viewModel.blockedAccountsState.isRefreshing,
        error = viewModel.blockedAccountsState.error,
        emptyStateText = stringResource(Res.string.no_blocked_accounts),
        onRefresh = { viewModel.getBlockedAccounts(true) },
        itemContent = { account ->
            Row { CustomBlockedAccountRow(account = account, navController = navController, viewModel = viewModel) }
        }
    )
}
