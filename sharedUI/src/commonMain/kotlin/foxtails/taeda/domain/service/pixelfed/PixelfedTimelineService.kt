package foxtails.taeda.domain.service.pixelfed

import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.general.TimelineService
import foxtails.taeda.domain.service.pixelfed.model.toDomain
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.domain.service.utils.loadPaginatedListResources
import me.tatarka.inject.annotations.Inject

@Inject
class PixelfedTimelineService(
    private val api: PixelfedApi,
    private val prefs: UserPreferences
): TimelineService {

    override fun getHomeTimeline(maxPostId: String?, enableReblogs: Boolean) =
        loadPaginatedListResources {
            api.getHomeTimeline(maxPostId, enableReblogs).map { it.toDomain() }
        }.filterSensitive(prefs.hideSensitiveContent)

    override fun getLocalTimeline(maxPostId: String?) = loadPaginatedListResources {
        api.getLocalTimeline(maxPostId).map { it.toDomain() }
    }.filterSensitive(prefs.hideSensitiveContent)

    override fun getGlobalTimeline(maxPostId: String?) = loadPaginatedListResources {
        api.getGlobalTimeline(maxPostId).map { it.toDomain() }
    }.filterSensitive(prefs.hideSensitiveContent)

    override fun getHashtagTimeline(
        hashtag: String,
        maxId: String?,
        limit: Int
    ) = loadPaginatedListResources {
        api.getHashtagTimeline(hashtag, maxId, limit).map { it.toDomain() }
    }.filterSensitive(prefs.hideSensitiveContent)

    override fun getCategoryTimeline(
        category: String,
        maxId: String?,
        limit: Int
    ) = loadPaginatedListResources<Post> {
        emptyList()
    }

    override fun getCameraTimeline(
        camera: String,
        maxId: String?,
        limit: Int
    ) = loadPaginatedListResources<Post> {
        emptyList()
    }

    override fun getFilmTimeline(
        film: String,
        maxId: String?,
        limit: Int
    ) = loadPaginatedListResources<Post> {
        emptyList()
    }

    override fun getLensTimeline(
        lens: String,
        maxId: String?,
        limit: Int
    ) = loadPaginatedListResources<Post> {
        emptyList()
    }
}