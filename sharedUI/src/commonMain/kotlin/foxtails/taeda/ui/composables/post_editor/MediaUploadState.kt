package foxtails.taeda.ui.composables.post_editor

import foxtails.taeda.domain.model.MediaAttachment

data class MediaUploadState(
    val isLoading: Boolean = false,
    val mediaAttachments: List<MediaAttachment> = emptyList(),
    val error: String = ""
)
