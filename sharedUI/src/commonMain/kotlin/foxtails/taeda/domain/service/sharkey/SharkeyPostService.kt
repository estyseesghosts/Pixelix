package foxtails.taeda.domain.service.sharkey

import foxtails.taeda.domain.model.Account
import foxtails.taeda.domain.model.NewReport
import foxtails.taeda.domain.model.PaginatedResponse
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.model.PostContext
import foxtails.taeda.domain.model.ReportResponse
import foxtails.taeda.domain.repository.sharkey.SharkeyApi
import foxtails.taeda.domain.service.general.AuthService
import foxtails.taeda.domain.service.general.PostService
import foxtails.taeda.domain.service.general.ReplyNode
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.sharkey.model.SharkeyRequest
import foxtails.taeda.domain.service.sharkey.model.toDomain
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.service.utils.loadPaginatedListResources
import foxtails.taeda.domain.service.utils.loadResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class SharkeyPostService(
    private val api: SharkeyApi,
    private val session: Session,
    private val prefs: UserPreferences,
    private val authService: AuthService
) : PostService {
    override fun getPostById(postId: String) = loadResource {
        api.getNote(request(noteId = postId)).toDomain()
    }

    override fun getOwnPosts(maxPostId: String?, limit: Int): Flow<Resource<PaginatedResponse<Post>>> {
        val accountId = authService.getCurrentSession()?.accountId
            ?: return flowOf(Resource.Error("No account found"))
        return getPostsByAccount(accountId, maxPostId, limit)
    }

    override fun getPostsOfAccount(
        accountId: String,
        username: String,
        maxPostId: String?,
        limit: Int
    ) = getPostsByAccount(accountId, maxPostId, limit).filterSensitive(prefs.hideSensitiveContent)

    override fun getLikedPosts(maxId: String?) = loadPaginatedListResources {
        api.getFavorites(request(untilId = maxId)).map { it.toDomain() }
    }.filterSensitive(prefs.hideSensitiveContent)

    override fun createReply(postId: String, content: String) = loadResource {
        api.createNote(request(text = content, replyId = postId)).toDomain()
    }

    override fun getReplies(postId: String): Flow<Resource<List<ReplyNode>>> = unsupported()

    override fun postContext(postId: String): Flow<Resource<PostContext>> = unsupported()

    override fun likePost(postId: String) = loadResource {
        api.favorite(request(noteId = postId))
        api.getNote(request(noteId = postId)).toDomain()
    }

    override fun unlikePost(postId: String) = loadResource {
        api.unfavorite(request(noteId = postId))
        api.getNote(request(noteId = postId)).toDomain()
    }

    override fun reblogPost(postId: String): Flow<Resource<Post>> = unsupported()

    override fun unreblogPost(postId: String): Flow<Resource<Post>> = unsupported()

    override fun bookmarkPost(postId: String): Flow<Resource<Post>> = unsupported()

    override fun unBookmarkPost(postId: String): Flow<Resource<Post>> = unsupported()

    override fun getBookmarkedPosts(cursor: String?): Flow<Resource<PaginatedResponse<Post>>> = unsupported()

    override fun reportPost(reportBody: NewReport): Flow<Resource<ReportResponse>> = unsupported()

    override fun getLikedBy(postId: String): Flow<Resource<PaginatedResponse<Account>>> = unsupported()

    private fun getPostsByAccount(accountId: String, untilId: String?, limit: Int) = loadPaginatedListResources {
        api.getUserNotes(request(userId = accountId, untilId = untilId, limit = limit)).map { it.toDomain() }
    }

    private fun request(
        noteId: String? = null,
        untilId: String? = null,
        limit: Int? = null,
        userId: String? = null,
        text: String? = null,
        replyId: String? = null
    ) = SharkeyRequest(
        i = requireNotNull(session.credentials.value?.token) { "No Sharkey session found." },
        noteId = noteId,
        untilId = untilId,
        limit = limit,
        userId = userId,
        text = text,
        replyId = replyId
    )

    private fun <T> unsupported(): Flow<Resource<T>> =
        flowOf(Resource.Error("This post action is not supported by Sharkey."))
}