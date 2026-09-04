package foxtails.taeda.domain.model.request

import foxtails.taeda.domain.service.pixelfed.model.request.PixelfedUpdateUserRequest

data class UpdateUserRequest(
    val displayName: String? = null,
    val note: String? = null,
    val website: String? = null,
    val manuallyAcceptNewFollowers: Boolean? = null,
    val includeProfilePageInSearchEngines: Boolean? = null,
    val includePublicPostsInSearchEngines: Boolean? = null,
    val locked: Boolean,
    val fields: List<UpdateFieldRequest> = emptyList()
)

data class UpdateFieldRequest(
    val id: String? = null,
    val key: String = "",
    val value: String = "",
    val valueHtml: String? = null,
    val isVerified: Boolean? = null
)

fun UpdateUserRequest.toPixelfed(): PixelfedUpdateUserRequest {
    return PixelfedUpdateUserRequest(
        displayName = this.displayName,
        note = this.note,
        website = this.website,
        locked = this.locked
    )
}
