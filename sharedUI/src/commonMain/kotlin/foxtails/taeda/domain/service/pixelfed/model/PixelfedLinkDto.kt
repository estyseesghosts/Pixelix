package foxtails.taeda.domain.service.pixelfed.model

import foxtails.taeda.domain.model.Link
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PixelfedLinkDto(
    @SerialName("href") val href: String,
    @SerialName("rel") val rel: String
)

fun PixelfedLinkDto.toDomain(): Link {
    return Link(
        href = this.href,
        rel = this.rel
    )
}

fun Link.toDto(): PixelfedLinkDto {
    return PixelfedLinkDto(
        href = this.href,
        rel = this.rel
    )
}