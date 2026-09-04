package foxtails.taeda.domain.service.pixelfed.model

import foxtails.taeda.domain.model.ReportResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PixelfedReportResponseDto(
    @SerialName("msg") val message: String,
    @SerialName("code") val code: Int
)

fun PixelfedReportResponseDto.toDomain(): ReportResponse {
    return ReportResponse(
        message = this.message,
        code = this.code
    )
}