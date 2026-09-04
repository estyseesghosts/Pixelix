package foxtails.taeda.domain.service.sharkey.model

import foxtails.taeda.domain.model.Account
import foxtails.taeda.domain.model.MediaAttachment
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.model.Tag
import foxtails.taeda.domain.model.Visibility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharkeyRequest(
    val i: String,
    val limit: Int? = null,
    @SerialName("untilId") val untilId: String? = null,
    @SerialName("withFiles") val withFiles: Boolean? = null,
    @SerialName("withRenotes") val withRenotes: Boolean? = null,
    @SerialName("userId") val userId: String? = null,
    @SerialName("userIds") val userIds: List<String>? = null,
    val query: String? = null,
    val detail: Boolean? = null,
    val text: String? = null,
    @SerialName("replyId") val replyId: String? = null,
    @SerialName("noteId") val noteId: String? = null,
    val tag: String? = null,
    @SerialName("recipientId") val recipientId: String? = null,
    @SerialName("messageId") val messageId: String? = null,
    @SerialName("markAsRead") val markAsRead: Boolean? = null,
    @SerialName("notificationId") val notificationId: String? = null,
    val visibility: String? = null,
    @SerialName("visibleUserIds") val visibleUserIds: List<String>? = null
)

@Serializable
data class SharkeyUserDto(
    val id: String? = null,
    val username: String? = null,
    val host: String? = null,
    val name: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null,
    @SerialName("bannerUrl") val bannerUrl: String? = null,
    val description: String? = null,
    @SerialName("followersCount") val followersCount: Long? = null,
    @SerialName("followingCount") val followingCount: Long? = null,
    @SerialName("notesCount") val notesCount: Long? = null,
    @SerialName("isLocked") val isLocked: Boolean? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    val url: String? = null
)

@Serializable
data class SharkeyFileDto(
    val id: String? = null,
    val url: String? = null,
    @SerialName("thumbnailUrl") val thumbnailUrl: String? = null,
    val type: String? = null,
    val comment: String? = null,
    val blurhash: String? = null,
    @SerialName("isSensitive") val isSensitive: Boolean? = null,
    val properties: SharkeyFilePropertiesDto? = null
)

@Serializable
data class SharkeyFilePropertiesDto(
    val width: Int? = null,
    val height: Int? = null
)

@Serializable
data class SharkeyNoteDto(
    val id: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    val text: String? = null,
    val cw: String? = null,
    val user: SharkeyUserDto? = null,
    @SerialName("userId") val userId: String? = null,
    val files: List<SharkeyFileDto>? = null,
    val tags: List<String>? = null,
    @SerialName("replyId") val replyId: String? = null,
    @SerialName("repliesCount") val repliesCount: Int? = null,
    @SerialName("renoteCount") val renoteCount: Int? = null,
    @SerialName("myReaction") val myReaction: String? = null,
    @SerialName("isFavorited") val isFavorited: Boolean? = null,
    val visibility: String? = null,
    @SerialName("visibleUserIds") val visibleUserIds: List<String>? = null,
    val renote: SharkeyNoteDto? = null,
    @SerialName("renoteId") val renoteId: String? = null,
    val quote: SharkeyNoteDto? = null,
    @SerialName("quoteId") val quoteId: String? = null,
    val url: String? = null
)

@Serializable
data class SharkeyRelationshipDto(
    @SerialName("id") val userId: String? = null,
    @SerialName("isFollowing") val isFollowing: Boolean? = null,
    @SerialName("isFollowed") val isFollowed: Boolean? = null,
    @SerialName("isBlocking") val isBlocking: Boolean? = null,
    @SerialName("isMuted") val isMuted: Boolean? = null
)

fun SharkeyUserDto.toDomain(): Account {
    val localUsername = username.orEmpty()
    val acct = if (host.isNullOrBlank()) localUsername else "$localUsername@$host"
    return Account(
        id = id.orEmpty(),
        username = acct,
        shortUsername = localUsername,
        acct = acct,
        displayname = name?.takeIf { it.isNotBlank() } ?: localUsername,
        avatar = avatarUrl,
        followersCount = followersCount.toCount(),
        followingCount = followingCount.toCount(),
        postsCount = notesCount.toCount(),
        website = url.orEmpty(),
        note = description.orEmpty(),
        url = url.orEmpty(),
        locked = isLocked ?: false,
        createdAt = createdAt.orEmpty(),
        headerUrl = bannerUrl
    )
}

fun List<SharkeyNoteDto>.toFeaturedAccounts(): List<Account> =
    mapNotNull { it.user?.takeIf { user -> !user.id.isNullOrBlank() } }
        .distinctBy { it.id }
        .map { it.toDomain() }

private fun Long?.toCount(): Int = this?.coerceIn(0, Int.MAX_VALUE.toLong())?.toInt() ?: 0

fun SharkeyFileDto.toDomain(): MediaAttachment {
    val dimensions = properties
    val aspectRatio = dimensions?.width?.toDouble()?.let { width ->
        dimensions.height?.takeIf { it > 0 }?.let { height -> width / height }
    }
    return MediaAttachment(
        id = id.orEmpty(),
        url = url.orEmpty(),
        previewUrl = thumbnailUrl,
        thumbnail = thumbnailUrl,
        aspectRatio = aspectRatio,
        metadata = null,
        blurHash = blurhash,
        type = type?.substringBefore('/'),
        description = comment,
        license = null,
        location = null
    )
}

fun SharkeyNoteDto.toDomain(): Post {
    val activeNote = renote ?: this
    return Post(
        id = id.orEmpty(),
        reblogId = renoteId ?: renote?.id,
        rebloggedBy = if (renote != null) user?.toDomain() else null,
        content = activeNote.text.orEmpty(),
        account = activeNote.user?.toDomain() ?: Account.unknown(),
        mediaAttachments = activeNote.files.orEmpty().map { it.toDomain() },
        tags = activeNote.tags.orEmpty().map { tag ->
            Tag(id = tag, name = tag, url = "", following = false, postsCount = null, hashtag = tag)
        },
        favouritesCount = 0,
        replyCount = activeNote.repliesCount ?: 0,
        createdAt = activeNote.createdAt.orEmpty(),
        url = activeNote.url.orEmpty(),
        sensitive = activeNote.files.orEmpty().any { it.isSensitive == true },
        spoilerText = activeNote.cw.orEmpty(),
        favourited = activeNote.isFavorited ?: (activeNote.myReaction != null),
        reblogged = false,
        bookmarked = activeNote.isFavorited ?: false,
        mentions = emptyList(),
        location = null,
        likedBy = null,
        visibility = activeNote.visibility.toVisibility(),
        inReplyToId = activeNote.replyId,
        reblogCount = activeNote.renoteCount ?: 0,
        emojis = emptyList(),
        commentsDisabled = false,
        category = null,
        isQuote = activeNote.quote != null || activeNote.quoteId != null
    )
}

private fun String?.toVisibility() = when (this) {
    "home" -> Visibility.UNLISTED
    "followers" -> Visibility.PRIVATE
    "specified" -> Visibility.DIRECT
    else -> Visibility.PUBLIC
}

@Serializable
data class SharkeyNotificationDto(
    val id: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    val type: String? = null,
    @SerialName("isRead") val isRead: Boolean? = null,
    val user: SharkeyUserDto? = null,
    val note: SharkeyNoteDto? = null
)

fun SharkeyNotificationDto.toDomain(): foxtails.taeda.domain.model.Notification =
    foxtails.taeda.domain.model.Notification(
        account = user?.toDomain() ?: note?.user?.toDomain() ?: Account.unknown(),
        id = id.orEmpty(),
        type = type.toNotificationType(),
        post = note?.toDomain(),
        createdAt = createdAt.orEmpty()
    )

private fun String?.toNotificationType() = when (this) {
    "mention", "quote" -> foxtails.taeda.domain.model.NotificationType.MENTION
    "reply" -> foxtails.taeda.domain.model.NotificationType.NEW_COMMENT
    "renote" -> foxtails.taeda.domain.model.NotificationType.REBLOG
    "reaction" -> foxtails.taeda.domain.model.NotificationType.FAVOURITE
    "follow" -> foxtails.taeda.domain.model.NotificationType.FOLLOW
    "receiveFollowRequest" -> foxtails.taeda.domain.model.NotificationType.FOLLOW_REQUEST
    else -> foxtails.taeda.domain.model.NotificationType.UNDEFINED
}

@Serializable
data class SharkeyMessageDto(
    val id: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    val text: String? = null,
    @SerialName("userId") val userId: String? = null,
    @SerialName("recipientId") val recipientId: String? = null,
    @SerialName("isRead") val isRead: Boolean? = null,
    val user: SharkeyUserDto? = null,
    val recipient: SharkeyUserDto? = null
)

@Serializable
data class SharkeyMessagingHistoryDto(
    val id: String? = null,
    @SerialName("isRead") val isRead: Boolean? = null,
    val user: SharkeyUserDto? = null,
    @SerialName("lastMessage") val lastMessage: SharkeyMessageDto? = null
)