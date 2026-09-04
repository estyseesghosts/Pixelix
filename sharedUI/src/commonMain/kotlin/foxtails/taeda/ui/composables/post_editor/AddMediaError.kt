package foxtails.taeda.ui.composables.post_editor

import foxtails.taeda.domain.model.request.MediaAttachmentMetadataRequest
import foxtails.taeda.utils.EmptyKmpUri
import foxtails.taeda.utils.KmpUri

data class AddMediaError(
    val type: AddMediaErrorType = AddMediaErrorType.NONE,
    val title: String = "",
    val description: String = "",
    val uri: KmpUri = EmptyKmpUri,
    val metadata: MediaAttachmentMetadataRequest? = null
)

enum class AddMediaErrorType {
    TOO_BIG_MEDIA,
    ERROR,
    NONE
}