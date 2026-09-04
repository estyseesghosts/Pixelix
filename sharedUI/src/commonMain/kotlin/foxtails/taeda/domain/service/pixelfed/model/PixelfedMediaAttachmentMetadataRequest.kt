package foxtails.taeda.domain.service.pixelfed.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PixelfedMediaAttachmentMetadataRequest(
    @SerialName("description") val description: String
)
