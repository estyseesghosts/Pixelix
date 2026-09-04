package foxtails.taeda.ui.composables.hashtagMentionText

import androidx.lifecycle.ViewModel
import foxtails.taeda.domain.service.general.AuthService
import me.tatarka.inject.annotations.Inject

class TextWithClickableHashtagsAndMentionsViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {
    suspend fun getMyAccountId(): String {
        return authService.getCurrentSession()!!.accountId
    }
}