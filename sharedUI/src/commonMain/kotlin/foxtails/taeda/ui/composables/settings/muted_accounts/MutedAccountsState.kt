package foxtails.taeda.ui.composables.settings.muted_accounts

import foxtails.taeda.domain.model.MutedAccount

data class MutedAccountsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val mutedAccounts: List<MutedAccount> = emptyList(),
    val error: String = ""
)
