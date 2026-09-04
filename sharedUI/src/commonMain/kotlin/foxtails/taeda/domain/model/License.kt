package foxtails.taeda.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val code: String?,
    val id: String?,
    val name: String?,
    val url: String?
)