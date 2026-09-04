package foxtails.taeda.ui.composables.edit_profile

import androidx.compose.ui.graphics.ImageBitmap
import foxtails.taeda.domain.model.Account

data class EditProfileState(
    val isLoading: Boolean = false,
    val account: Account? = null,
    val error: String = ""
)
