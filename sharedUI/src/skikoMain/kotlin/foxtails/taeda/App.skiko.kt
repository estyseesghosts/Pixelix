package foxtails.taeda

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
actual fun EdgeToEdgeDialogProperties(
    dismissOnBackPress: Boolean,
    dismissOnClickOutside: Boolean,
    usePlatformDefaultWidth: Boolean
): DialogProperties = DialogProperties(
    dismissOnBackPress = dismissOnBackPress,
    dismissOnClickOutside = dismissOnClickOutside,
    usePlatformDefaultWidth = usePlatformDefaultWidth,
    usePlatformInsets = false
)
