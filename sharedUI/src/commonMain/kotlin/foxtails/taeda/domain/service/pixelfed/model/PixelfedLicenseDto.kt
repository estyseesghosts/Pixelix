package foxtails.taeda.domain.service.pixelfed.model

import foxtails.taeda.domain.model.License
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PixelfedLicenseDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("url") val url: String
)

fun PixelfedLicenseDto.toDomain(): License {
    return License(
        id = this.id.toString(),
        name = this.title,
        code = null,
        url = this.url
    )
}