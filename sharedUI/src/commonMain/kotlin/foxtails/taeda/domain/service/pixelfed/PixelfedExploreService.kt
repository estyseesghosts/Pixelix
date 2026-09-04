package foxtails.taeda.domain.service.pixelfed

import foxtails.taeda.domain.model.Account
import foxtails.taeda.domain.model.Camera
import foxtails.taeda.domain.model.Category
import foxtails.taeda.domain.model.Country
import foxtails.taeda.domain.model.Film
import foxtails.taeda.domain.model.Lens
import foxtails.taeda.domain.model.License
import foxtails.taeda.domain.model.PagePaginatedResponse
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.general.ExploreService
import foxtails.taeda.domain.service.pixelfed.model.toDomain
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.service.utils.loadListResources
import foxtails.taeda.domain.service.utils.loadPaginatedListResources
import foxtails.taeda.domain.service.utils.loadResource
import foxtails.taeda.ui.composables.explore.trending.TrendingRange
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class PixelfedExploreService(
    private val prefs: UserPreferences, private val api: PixelfedApi
) : ExploreService {
    override fun getTrendingAccounts(range: TrendingRange, maxId: String?) =
        loadPaginatedListResources {
            if (maxId == null) {
                api.getTrendingAccounts().map { it.toDomain() }
            } else {
                emptyList()
            }
        }

    override fun getTrendingPosts(range: TrendingRange, maxId: String?) =
        loadPaginatedListResources {
            if (maxId == null) {
                api.getTrendingPosts(range.toApiString()).map { it.toDomain() }
            } else {
                emptyList()
            }
        }.filterSensitive(prefs.hideSensitiveContent)

    override fun search(searchText: String, type: String?, limit: Int) = loadResource {
        api.getSearch(searchText, type, limit).toDomain()
    }

    override fun searchLocations(searchText: String, countryCode: String?) = loadListResources {
        api.searchLocations(searchText).map { it.toDomain() }
    }

    override fun getAllCountries(): Flow<Resource<List<Country>>> = loadResource {
        emptyList()
    }

    override fun getTrendingHashtags(range: TrendingRange, maxId: String?) =
        loadPaginatedListResources {
            if (maxId == null) {
                api.getTrendingHashtags().map { it.toDomain() }
            } else {
                emptyList()
            }

        }

    override fun getFollowedHashtags() = loadListResources {
        api.getFollowedHashtags().map { it.toDomain() }
    }

    override fun getRelatedHashtags(hashtag: String) = loadListResources {
        api.getRelatedHashtags(hashtag).map { it.toDomain() }
    }

    override fun getHashtag(hashtag: String) = loadResource {
        api.getHashtag(hashtag).toDomain()
    }

    override fun followHashtag(tagId: String) = loadResource {
        api.followHashtag(tagId).toDomain()
    }

    override fun unfollowHashtag(tagId: String) = loadResource {
        api.unfollowHashtag(tagId)
        Unit
    }

    override fun getEditorsChoicePosts(
        maxId: String?,
    ) = loadPaginatedListResources<Post> {
        emptyList()
    }

    override fun getEditorsChoiceAccounts(
        maxId: String?,
    ) = loadPaginatedListResources<Account> {
        emptyList()
    }

    override fun getCategories(): Flow<Resource<List<Category>>> = loadListResources {
        emptyList()
    }

    override fun getCameras(page: Int, size: Int): Flow<Resource<PagePaginatedResponse<Camera>>> =
        loadResource {
            PagePaginatedResponse(
                data = emptyList(),
                currentPage = page,
                size = size,
                total = 0
            )
        }

    override fun getLenses(page: Int, size: Int): Flow<Resource<PagePaginatedResponse<Lens>>> =
        loadResource {
            PagePaginatedResponse(
                data = emptyList(),
                currentPage = page,
                size = size,
                total = 0
            )
        }

    override fun getFilms(page: Int, size: Int): Flow<Resource<PagePaginatedResponse<Film>>> =
        loadResource {
            PagePaginatedResponse(
                data = emptyList(),
                currentPage = page,
                size = size,
                total = 0
            )
        }

    override fun getLicenses(): Flow<Resource<List<License>>> = loadListResources {
        emptyList()
    }
}