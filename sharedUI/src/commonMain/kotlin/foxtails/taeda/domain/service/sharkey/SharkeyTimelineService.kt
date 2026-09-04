package foxtails.taeda.domain.service.sharkey

import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.repository.sharkey.SharkeyApi
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.general.TimelineService
import foxtails.taeda.domain.service.sharkey.model.SharkeyRequest
import foxtails.taeda.domain.service.sharkey.model.toDomain
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.service.utils.loadPaginatedListResources
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class SharkeyTimelineService(
    private val api: SharkeyApi,
    private val session: Session,
    private val prefs: UserPreferences
) : TimelineService {
    override fun getHomeTimeline(maxPostId: String?, enableReblogs: Boolean) =
        loadPaginatedListResources {
            api.getHomeTimeline(request(untilId = maxPostId, withRenotes = enableReblogs)).map { it.toDomain() }
        }.filterSensitive(prefs.hideSensitiveContent)

    override fun getLocalTimeline(maxPostId: String?) = loadPaginatedListResources {
        api.getLocalTimeline(request(untilId = maxPostId)).map { it.toDomain() }
    }.filterSensitive(prefs.hideSensitiveContent)

    override fun getGlobalTimeline(maxPostId: String?) = loadPaginatedListResources {
        api.getGlobalTimeline(request(untilId = maxPostId)).map { it.toDomain() }
    }.filterSensitive(prefs.hideSensitiveContent)

    override fun getHashtagTimeline(hashtag: String, maxId: String?, limit: Int) = loadPaginatedListResources {
        api.getHashtagTimeline(request(untilId = maxId, limit = limit, tag = hashtag)).map { it.toDomain() }
    }.filterSensitive(prefs.hideSensitiveContent)

    override fun getCategoryTimeline(category: String, maxId: String?, limit: Int) = unsupported()

    override fun getCameraTimeline(camera: String, maxId: String?, limit: Int) = unsupported()

    override fun getLensTimeline(lens: String, maxId: String?, limit: Int) = unsupported()

    override fun getFilmTimeline(film: String, maxId: String?, limit: Int) = unsupported()

    private fun request(
        untilId: String? = null,
        limit: Int = DEFAULT_LIMIT,
        withRenotes: Boolean? = null,
        tag: String? = null
    ) = SharkeyRequest(
        i = requireNotNull(session.credentials.value?.token) { "No Sharkey session found." },
        untilId = untilId,
        limit = limit,
        withRenotes = withRenotes,
        tag = tag
    )

    private fun unsupported() = flowOf(
        Resource.Error<foxtails.taeda.domain.model.PaginatedResponse<Post>>(
            "This timeline is not supported by Sharkey."
        )
    )

    private companion object {
        const val DEFAULT_LIMIT = 20
    }
}