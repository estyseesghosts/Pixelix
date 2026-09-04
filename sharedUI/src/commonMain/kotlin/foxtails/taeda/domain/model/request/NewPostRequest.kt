package foxtails.taeda.domain.model.request

import foxtails.taeda.domain.model.Visibility
import foxtails.taeda.domain.model.toPixelfed
import foxtails.taeda.domain.service.pixelfed.model.request.PixelfedNewPostRequest

data class NewPostRequest(
    val note: String,
    val mediaIds: List<String> = emptyList(),
    val sensitive: Boolean,
    val contentWarning: String?,
    val visibility: Visibility,
    val placeId: String?,
    val commentsDisabled: Boolean,
    val categoryId: String?
)

fun NewPostRequest.toPixelfed(): PixelfedNewPostRequest {
    return PixelfedNewPostRequest(
        status = this.note,
        mediaIds = this.mediaIds,
        sensitive = this.sensitive,
        visibility = this.visibility.toPixelfed(),
        spoilerText = this.contentWarning,
        placeId = this.placeId,
        commentsDisabled = this.commentsDisabled
    )
}
