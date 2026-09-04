package foxtails.taeda.ui.composables.profile

import foxtails.taeda.domain.model.Account

data class AccountState(
    val isLoading: Boolean = false,
    val refreshing: Boolean = false,
    val account: Account? = null,
    val error: String = ""
)