package foxtails.taeda.ui.events

import foxtails.taeda.di.AppSingleton
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import me.tatarka.inject.annotations.Inject

/**
 * Open account intent flow
 * Pair<AccountId, String>
 */
@Inject
@AppSingleton
class AccountIntentHandler {
    private val _pendingAccount = MutableSharedFlow<Pair<String, String>>(extraBufferCapacity = 1)
    val pendingAccount = _pendingAccount.asSharedFlow()

    fun onAccountOpen(accountId: String, username: String) {
        _pendingAccount.tryEmit(Pair(accountId, username))
    }
}