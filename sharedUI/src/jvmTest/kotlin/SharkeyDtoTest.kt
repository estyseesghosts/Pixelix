package foxtails.taeda

import foxtails.taeda.domain.model.Visibility
import foxtails.taeda.domain.service.sharkey.model.SharkeyNoteDto
import foxtails.taeda.domain.service.sharkey.model.SharkeyMessagingHistoryDto
import foxtails.taeda.domain.service.sharkey.model.SharkeyNotificationDto
import foxtails.taeda.domain.service.sharkey.model.SharkeyRequest
import foxtails.taeda.domain.service.sharkey.model.SharkeyUserDto
import foxtails.taeda.domain.service.sharkey.model.toFeaturedAccounts
import foxtails.taeda.domain.service.sharkey.model.toDomain
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SharkeyDtoTest {

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun sparseNoteAndUserPayloadsMapWithoutRequiredMetadata() {
        val note = json.decodeFromString<SharkeyNoteDto>(
            """{"id":"note","user":{"id":"user","username":"shark"},"files":[{"id":"file","url":"https://example.com/file.mp3","type":"audio/mpeg"}]}"""
        ).toDomain()

        assertEquals("note", note.id)
        assertEquals("shark", note.account.username)
        assertEquals("audio", note.mediaAttachments.single().type)
        assertNull(note.mediaAttachments.single().aspectRatio)
        assertFalse(note.sensitive)
    }

    @Test
    fun renotesKeepOriginalContentAndContentWarning() {
        val post = SharkeyNoteDto(
            id = "renote",
            user = SharkeyUserDto(id = "booster", username = "booster"),
            renote = SharkeyNoteDto(
                id = "original",
                text = "caption",
                cw = "warning",
                visibility = "followers",
                user = SharkeyUserDto(id = "author", username = "author"),
                isFavorited = true
            )
        ).toDomain()

        assertEquals("original", post.reblogId)
        assertEquals("caption", post.content)
        assertEquals("warning", post.spoilerText)
        assertEquals(Visibility.PRIVATE, post.visibility)
        assertTrue(post.favourited)
    }

    @Test
    fun quoteStatusIsPreservedFromSharkeyNote() {
        val post = SharkeyNoteDto(
            id = "quote",
            user = SharkeyUserDto(id = "author", username = "author"),
            quoteId = "quoted"
        ).toDomain()

        assertTrue(post.isQuote)
    }

    @Test
    fun authenticatedRequestsContainOnlyTheProvidedNativeCursorAndToken() {
        val request = SharkeyRequest(i = "token", untilId = "last-note", limit = 20)
        val encoded = json.encodeToString(request)

        assertTrue(encoded.contains("\"i\":\"token\""))
        assertTrue(encoded.contains("\"untilId\":\"last-note\""))
        assertFalse(encoded.contains("userId"))
    }

    @Test
    fun featuredUserPayloadSupportsLargeCountsAndSparseProfileFields() {
        val account = json.decodeFromString<SharkeyUserDto>(
            """{"id":"user","username":"featured","followersCount":2147483648,"notesCount":2,"isLocked":false,"avatarUrl":"https://example.com/avatar.webp"}"""
        ).toDomain()

        assertEquals("featured", account.username)
        assertEquals(Int.MAX_VALUE, account.followersCount)
        assertEquals(2, account.postsCount)
        assertEquals("https://example.com/avatar.webp", account.avatar)
    }

    @Test
    fun featuredNotesProduceDistinctProfileAuthorsInFeaturedOrder() {
        val accounts = listOf(
            SharkeyNoteDto(id = "note-1", user = SharkeyUserDto(id = "first", username = "first")),
            SharkeyNoteDto(id = "note-2", user = SharkeyUserDto(id = "first", username = "first")),
            SharkeyNoteDto(id = "note-3", user = SharkeyUserDto(id = "second", username = "second")),
            SharkeyNoteDto(id = "note-4", user = SharkeyUserDto(username = "missing-id"))
        ).toFeaturedAccounts()

        assertEquals(listOf("first", "second"), accounts.map { it.id })
    }

    @Test
    fun sparseMessagingHistoryDecodesForUnsupportedInstanceErrorHandling() {
        val history = json.decodeFromString<SharkeyMessagingHistoryDto>(
            """{"id":"peer","user":{"id":"peer","username":"friend"},"lastMessage":{"id":"message","text":"hello","userId":"peer"}}"""
        )

        assertEquals("peer", history.user?.id)
        assertEquals("hello", history.lastMessage?.text)
        assertFalse(history.isRead ?: false)
    }

    @Test
    fun notificationPayloadMapsNativeTypesAndReadState() {
        val notification = json.decodeFromString<SharkeyNotificationDto>(
            """{"id":"notification","createdAt":"2026-09-03T08:00:00.000Z","type":"reaction","isRead":false,"user":{"id":"user","username":"reactor"},"note":{"id":"note","text":"photo","user":{"id":"author","username":"author"}}}"""
        )

        assertFalse(notification.isRead ?: true)
        assertEquals("reactor", notification.toDomain().account.username)
        assertEquals(foxtails.taeda.domain.model.NotificationType.FAVOURITE, notification.toDomain().type)
        assertEquals("note", notification.toDomain().post?.id)
    }

    @Test
    fun specifiedNoteRequestIncludesRecipientVisibilityAndCursor() {
        val encoded = json.encodeToString(
            SharkeyRequest(
                i = "token",
                text = "hello",
                visibility = "specified",
                visibleUserIds = listOf("recipient"),
                untilId = "previous"
            )
        )

        assertTrue(encoded.contains("\"visibility\":\"specified\""))
        assertTrue(encoded.contains("\"visibleUserIds\":[\"recipient\"]"))
        assertTrue(encoded.contains("\"untilId\":\"previous\""))
    }
}