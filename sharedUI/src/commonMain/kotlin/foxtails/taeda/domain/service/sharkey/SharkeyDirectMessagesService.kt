package foxtails.taeda.domain.service.sharkey

import foxtails.taeda.domain.model.Chat
import foxtails.taeda.domain.model.Conversation
import foxtails.taeda.domain.model.Message
import foxtails.taeda.domain.model.NewMessage
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.model.Visibility
import foxtails.taeda.domain.repository.sharkey.SharkeyApi
import foxtails.taeda.domain.service.general.DirectMessagesService
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.sharkey.model.SharkeyNoteDto
import foxtails.taeda.domain.service.sharkey.model.SharkeyRequest
import foxtails.taeda.domain.service.sharkey.model.toDomain
import foxtails.taeda.domain.service.utils.loadListResources
import foxtails.taeda.domain.service.utils.loadResource
import me.tatarka.inject.annotations.Inject

@Inject
class SharkeyDirectMessagesService(
    private val api: SharkeyApi,
    private val session: Session
) : DirectMessagesService {
    override fun getConversations() = loadListResources {
        api.getMentionedNotes(request())
            .filter { it.visibility == VISIBILITY_SPECIFIED && it.user?.id != null }
            .groupBy { requireNotNull(it.user?.id) }
            .map { (_, notes) -> notes.maxByOrNull { it.createdAt.orEmpty() }!!.toConversation() }
            .sortedByDescending { it.lastPost.createdAt }
    }

    override fun getChat(accountId: String, maxId: String?) = loadResource {
        val account = api.getUser(request(userId = accountId)).toDomain()
        val messages = api.getMentionedNotes(request(untilId = maxId))
            .filter { it.visibility == VISIBILITY_SPECIFIED && it.user?.id == accountId }
        Chat(
            avatar = account.avatar.orEmpty(),
            id = account.id,
            isLocal = !account.acct.contains("@"),
            messages = messages.map { it.toMessage() },
            muted = false,
            name = account.displayname.orEmpty(),
            timeAgo = messages.firstOrNull()?.createdAt.orEmpty(),
            url = account.url,
            username = account.username
        )
    }

    override fun sendMessage(createMessageDto: NewMessage) = loadResource {
        api.createNote(request(text = createMessageDto.message, visibleUserIds = listOf(createMessageDto.toId))).toMessage()
    }

    override fun deleteMessage(id: String) = loadResource {
        api.deleteNote(request(noteId = id))
        emptyList<Int>()
    }

    private fun SharkeyNoteDto.toConversation(): Conversation {
        val account = user!!.toDomain()
        return Conversation(
            id = account.id.hashCode(),
            unread = false,
            accounts = listOf(account),
            lastPost = toPost()
        )
    }

    private fun SharkeyNoteDto.toPost(): Post = Post(
        id = id.orEmpty(),
        mediaAttachments = emptyList(),
        account = user?.toDomain() ?: foxtails.taeda.domain.model.Account.unknown(),
        tags = emptyList(),
        favouritesCount = 0,
        content = text.orEmpty(),
        replyCount = 0,
        createdAt = createdAt.orEmpty(),
        url = "",
        sensitive = false,
        spoilerText = "",
        favourited = false,
        reblogged = false,
        bookmarked = false,
        mentions = emptyList(),
        location = null,
        likedBy = null,
        visibility = Visibility.DIRECT,
        inReplyToId = null,
        reblogCount = 0,
        emojis = emptyList(),
        commentsDisabled = false,
        category = null
    )

    private fun SharkeyNoteDto.toMessage() = Message(
        hidden = false,
        id = id.orEmpty(),
        isAuthor = userId == session.credentials.value?.accountId,
        reportId = "",
        seen = false,
        text = text.orEmpty(),
        timeAgo = createdAt.orEmpty(),
        type = "text"
    )

    private fun request(
        userId: String? = null,
        untilId: String? = null,
        text: String? = null,
        noteId: String? = null,
        visibleUserIds: List<String>? = null
    ) = SharkeyRequest(
        i = requireNotNull(session.credentials.value?.token) { "No Sharkey session found." },
        userId = userId,
        untilId = untilId,
        text = text,
        noteId = noteId,
        visibility = VISIBILITY_SPECIFIED,
        visibleUserIds = visibleUserIds
    )

    private companion object {
        const val VISIBILITY_SPECIFIED = "specified"
    }
}