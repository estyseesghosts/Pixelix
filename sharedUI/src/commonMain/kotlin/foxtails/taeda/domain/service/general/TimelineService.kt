package foxtails.taeda.domain.service.general

import foxtails.taeda.di.AppSingleton
import foxtails.taeda.domain.model.PaginatedResponse
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.pixelfed.PixelfedTimelineService
import foxtails.taeda.domain.service.sharkey.SharkeyTimelineService
import foxtails.taeda.domain.service.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject


interface TimelineService {
    fun getHomeTimeline(
        maxPostId: String? = null, enableReblogs: Boolean = false
    ): Flow<Resource<PaginatedResponse<Post>>>

    fun getLocalTimeline(maxPostId: String? = null): Flow<Resource<PaginatedResponse<Post>>>

    fun getGlobalTimeline(maxPostId: String? = null): Flow<Resource<PaginatedResponse<Post>>>

    fun getHashtagTimeline(
        hashtag: String,
        maxId: String? = null,
        limit: Int = PixelfedApi.HASHTAG_TIMELINE_POSTS_LIMIT
    ): Flow<Resource<PaginatedResponse<Post>>>

    fun getCategoryTimeline(
        category: String,
        maxId: String? = null,
        limit: Int = PixelfedApi.HASHTAG_TIMELINE_POSTS_LIMIT
    ): Flow<Resource<PaginatedResponse<Post>>>

    fun getCameraTimeline(
        camera: String,
        maxId: String? = null,
        limit: Int = PixelfedApi.HASHTAG_TIMELINE_POSTS_LIMIT
    ): Flow<Resource<PaginatedResponse<Post>>>

    fun getLensTimeline(
        lens: String,
        maxId: String? = null,
        limit: Int = PixelfedApi.HASHTAG_TIMELINE_POSTS_LIMIT
    ): Flow<Resource<PaginatedResponse<Post>>>

    fun getFilmTimeline(
        film: String,
        maxId: String? = null,
        limit: Int = PixelfedApi.HASHTAG_TIMELINE_POSTS_LIMIT
    ): Flow<Resource<PaginatedResponse<Post>>>

    fun Flow<Resource<PaginatedResponse<Post>>>.filterSensitive(hideSensitiveContent: Boolean) =
        this.map { event ->
            if (event is Resource.Success<PaginatedResponse<Post>>) {
                val filtered = event.data.data.filter { !(hideSensitiveContent && it.sensitive) }
                Resource.Success(event.data.copy(data = filtered))
            } else {
                event
            }
        }
}

fun Flow<Resource<PaginatedResponse<Post>>>.filterTextPosts() = map { event ->
    if (event is Resource.Success<PaginatedResponse<Post>>) {
        Resource.Success(
            event.data.copy(
                data = event.data.data.filter { post ->
                    post.mediaAttachments.any { attachment ->
                        attachment.type in setOf(null, "image", "video", "gifv")
                    }
                }
            )
        )
    } else {
        event
    }
}

@Inject
@AppSingleton
class TimelineServiceDelegate(
    private val session: Session,
    private val pixelfed: PixelfedTimelineService,
    private val sharkey: SharkeyTimelineService
) : TimelineService {

    private val current: TimelineService
        get() = when (session.backendType.value) {
            BackendType.SHARKEY -> sharkey
            BackendType.MASTODON -> pixelfed
        }

    override fun getHomeTimeline(maxPostId: String?, enableReblogs: Boolean) =
        current.getHomeTimeline(maxPostId, enableReblogs)

    override fun getLocalTimeline(maxPostId: String?) = current.getLocalTimeline(maxPostId)

    override fun getGlobalTimeline(maxPostId: String?) = current.getGlobalTimeline(maxPostId)

    override fun getHashtagTimeline(
        hashtag: String, maxId: String?, limit: Int
    ) = current.getHashtagTimeline(hashtag, maxId, limit)

    override fun getCategoryTimeline(
        category: String, maxId: String?, limit: Int
    ) = current.getCategoryTimeline(category, maxId, limit)

    override fun getCameraTimeline(
        camera: String, maxId: String?, limit: Int
    ) = current.getCameraTimeline(camera, maxId, limit)

    override fun getLensTimeline(
        lens: String, maxId: String?, limit: Int
    ) = current.getLensTimeline(lens, maxId, limit)

    override fun getFilmTimeline(
        film: String, maxId: String?, limit: Int
    ) = current.getFilmTimeline(film, maxId, limit)
}