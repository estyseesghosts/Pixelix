package foxtails.taeda.ui.composables.settings.blocked_accounts

import foxtails.taeda.domain.model.Account

data class BlockedAccountsState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val blockedAccounts: List<Account> = emptyList(),
    val error: String = ""
)
