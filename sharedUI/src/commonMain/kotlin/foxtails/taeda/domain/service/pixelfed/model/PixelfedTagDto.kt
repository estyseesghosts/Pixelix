package foxtails.taeda.domain.service.pixelfed.model

import foxtails.taeda.domain.model.Tag
import foxtails.taeda.domain.repository.serializers.TagNameSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PixelfedTagDto(
    @Serializable(with = TagNameSerializer::class)
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
    @SerialName("following") val following: Boolean = false,
    @SerialName("count") val count: Int?,
    @SerialName("hashtag") val hashtag: String? = null
)

fun PixelfedTagDto.toDomain(): Tag {
    return Tag(
        name = this.name,
        url = this.url,
        following = this.following,
        postsCount = this.count,
        hashtag = this.hashtag,
        id = ""
    )
}