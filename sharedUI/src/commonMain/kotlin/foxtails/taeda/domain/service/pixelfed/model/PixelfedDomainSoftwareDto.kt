package foxtails.taeda.domain.service.pixelfed.model

import foxtails.taeda.domain.model.DomainSoftware
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PixelfedDomainSoftwareDto(
    @SerialName("name") val name: String,
    @SerialName("version") val version: String?
)

fun PixelfedDomainSoftwareDto.toDomain(): DomainSoftware {
    return DomainSoftware(
        name = this.name,
        version = this.version
    )
}

fun DomainSoftware.toDto(): PixelfedDomainSoftwareDto {
    return PixelfedDomainSoftwareDto(
        name = this.name,
        version = this.version
    )
}