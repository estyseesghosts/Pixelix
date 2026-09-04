package foxtails.taeda.domain.service.sharkey

import foxtails.taeda.domain.model.Account
import foxtails.taeda.domain.model.Camera
import foxtails.taeda.domain.model.Category
import foxtails.taeda.domain.model.Country
import foxtails.taeda.domain.model.Film
import foxtails.taeda.domain.model.Lens
import foxtails.taeda.domain.model.License
import foxtails.taeda.domain.model.Location
import foxtails.taeda.domain.model.PagePaginatedResponse
import foxtails.taeda.domain.model.PaginatedResponse
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.model.RelatedHashtag
import foxtails.taeda.domain.model.Search
import foxtails.taeda.domain.model.Tag
import foxtails.taeda.domain.repository.sharkey.SharkeyApi
import foxtails.taeda.domain.service.general.ExploreService
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.sharkey.model.SharkeyRequest
import foxtails.taeda.domain.service.sharkey.model.toFeaturedAccounts
import foxtails.taeda.domain.service.sharkey.model.toDomain
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.service.utils.loadPaginatedListResources
import foxtails.taeda.domain.service.utils.loadResource
import foxtails.taeda.ui.composables.explore.trending.TrendingRange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class SharkeyExploreService(
    private val api: SharkeyApi,
    private val session: Session,
    private val prefs: UserPreferences
) : ExploreService {
    override fun getTrendingAccounts(range: TrendingRange, maxId: String?) = terminal(maxId) {
        api.getFeaturedNotes(request()).toFeaturedAccounts()
    }

    override fun getTrendingPosts(range: TrendingRange, maxId: String?) = loadPaginatedListResources {
        api.getFeaturedNotes(request(untilId = maxId)).map { it.toDomain() }
    }.filterSensitive(prefs.hideSensitiveContent)

    override fun search(searchText: String, type: String?, limit: Int) = loadResource {
        when (type) {
            "accounts" -> Search(
                accounts = api.searchUsers(request(query = searchText, limit = limit, detail = true)).map { it.toDomain() },
                posts = emptyList(),
                tags = emptyList()
            )
            "statuses", "posts" -> Search(
                accounts = emptyList(),
                posts = api.searchNotes(request(query = searchText, limit = limit)).map { it.toDomain() },
                tags = emptyList()
            )
            "hashtags" -> Search(emptyList(), emptyList(), emptyList())
            null -> Search(
                accounts = api.searchUsers(request(query = searchText, limit = limit, detail = true)).map { it.toDomain() },
                posts = api.searchNotes(request(query = searchText, limit = limit)).map { it.toDomain() },
                tags = emptyList()
            )
            else -> Search(emptyList(), emptyList(), emptyList())
        }
    }

    override fun searchLocations(searchText: String, countryCode: String?): Flow<Resource<List<Location>>> = unsupported()

    override fun getAllCountries(): Flow<Resource<List<Country>>> = unsupported()

    override fun getTrendingHashtags(range: TrendingRange, maxId: String?): Flow<Resource<PaginatedResponse<Tag>>> = unsupported()

    override fun getFollowedHashtags(): Flow<Resource<List<Tag>>> = unsupported()

    override fun getRelatedHashtags(hashtag: String): Flow<Resource<List<RelatedHashtag>>> = unsupported()

    override fun getHashtag(hashtag: String): Flow<Resource<Tag>> = unsupported()

    override fun followHashtag(tagId: String): Flow<Resource<Tag>> = unsupported()

    override fun unfollowHashtag(tagId: String): Flow<Resource<Unit>> = unsupported()

    override fun getEditorsChoicePosts(maxId: String?): Flow<Resource<PaginatedResponse<Post>>> = unsupported()

    override fun getEditorsChoiceAccounts(maxId: String?): Flow<Resource<PaginatedResponse<Account>>> = unsupported()

    override fun getCategories(): Flow<Resource<List<Category>>> = unsupported()

    override fun getCameras(page: Int, size: Int): Flow<Resource<PagePaginatedResponse<Camera>>> = unsupported()

    override fun getLenses(page: Int, size: Int): Flow<Resource<PagePaginatedResponse<Lens>>> = unsupported()

    override fun getFilms(page: Int, size: Int): Flow<Resource<PagePaginatedResponse<Film>>> = unsupported()

    override fun getLicenses(): Flow<Resource<List<License>>> = unsupported()

    private fun request(
        query: String? = null,
        limit: Int = DEFAULT_LIMIT,
        detail: Boolean? = null,
        untilId: String? = null
    ) = SharkeyRequest(
        i = requireNotNull(session.credentials.value?.token) { "No Sharkey session found." },
        query = query,
        limit = limit,
        detail = detail,
        untilId = untilId
    )

    private fun <T> terminal(
        cursor: String?, call: suspend () -> List<T>
    ): Flow<Resource<PaginatedResponse<T>>> = if (cursor != null) {
        flowOf(Resource.Success(PaginatedResponse(emptyList(), next = null)))
    } else {
        loadResource(call).map { result ->
            when (result) {
                is Resource.Success -> Resource.Success(PaginatedResponse(result.data, next = null))
                is Resource.Loading -> Resource.Loading()
                is Resource.Error -> Resource.Error(result.message)
            }
        }
    }

    private fun <T> unsupported(): Flow<Resource<T>> =
        flowOf(Resource.Error("This Explore feature is not supported by Sharkey."))

    private companion object {
        const val DEFAULT_LIMIT = 20
    }
}