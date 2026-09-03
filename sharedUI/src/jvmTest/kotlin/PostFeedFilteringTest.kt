package com.daniebeler.pfpixelix

import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.MediaAttachment
import com.daniebeler.pfpixelix.domain.model.PaginatedResponse
import com.daniebeler.pfpixelix.domain.model.Post
import com.daniebeler.pfpixelix.domain.service.general.filterTextPosts
import com.daniebeler.pfpixelix.domain.service.pixelfed.model.PixelfedOriginalDto
import com.daniebeler.pfpixelix.domain.service.utils.Resource
import com.daniebeler.pfpixelix.ui.composables.profile.ViewEnum
import com.daniebeler.pfpixelix.ui.composables.profile.forPresentation
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class PostFeedFilteringTest {

    @Test
    fun mastodonMediaWithoutAspectRatioIsDecoded() {
        val media = Json.decodeFromString<PixelfedOriginalDto>("{}")

        assertNull(media.aspect)
    }

    @Test
    fun filterTextPostsKeepsPhotoAndVideoPostsAndPreservesCursor() = runBlocking {
        val events = flowOf(
            Resource.Loading(),
            Resource.Success(
                PaginatedResponse(
                    data = listOf(
                        post("text"),
                        post("image", "image"),
                        post("video", "video"),
                        post("gifv", "gifv"),
                        post("vernissage", null),
                        post("audio", "audio")
                    ),
                    next = "next-page"
                )
            )
        ).filterTextPosts().toList()

        assertIs<Resource.Loading<PaginatedResponse<Post>>>(events.first())
        val result = assertIs<Resource.Success<PaginatedResponse<Post>>>(events.last()).data
        assertEquals(listOf("image", "video", "gifv", "vernissage"), result.data.map { it.id })
        assertEquals("next-page", result.next)
    }

    @Test
    fun onlyVisualPresentationsExcludeTextOnlyAndAudioPosts() {
        val posts = listOf(
            post("text", "missing"),
            post("audio", "audio"),
            post("image", "image"),
            post("video", "video")
        )

        assertEquals(posts, posts.forPresentation(ViewEnum.Timeline))
        assertEquals(listOf("image", "video"), posts.forPresentation(ViewEnum.Grid).map { it.id })
        assertEquals(listOf("image", "video"), posts.forPresentation(ViewEnum.Masonry).map { it.id })
        assertEquals(listOf("image", "video"), posts.forPresentation(ViewEnum.LargeMasonry).map { it.id })
    }

    private fun post(id: String, attachmentType: String? = "missing"): Post = Post(
        id = id,
        mediaAttachments = if (attachmentType == "missing") emptyList() else listOf(
            MediaAttachment(id, "", null, null, null, null, null, attachmentType, null, null, null)
        ),
        account = Account(),
        tags = emptyList(),
        favouritesCount = 0,
        content = "",
        replyCount = 0,
        createdAt = "",
        url = "",
        sensitive = false,
        spoilerText = "",
        favourited = false,
        reblogged = false,
        bookmarked = false,
        mentions = emptyList(),
        location = null,
        likedBy = null,
        visibility = com.daniebeler.pfpixelix.domain.model.Visibility.PUBLIC,
        inReplyToId = null,
        reblogCount = 0,
        emojis = emptyList(),
        commentsDisabled = false,
        category = null
    )
}