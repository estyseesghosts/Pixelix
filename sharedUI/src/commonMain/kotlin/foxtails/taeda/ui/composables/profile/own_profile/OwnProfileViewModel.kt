package foxtails.taeda.ui.composables.profile.own_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import foxtails.taeda.domain.model.Account
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.general.AccountService
import foxtails.taeda.domain.service.general.AppIconService
import foxtails.taeda.domain.service.general.AuthService
import foxtails.taeda.domain.service.general.BackendType
import foxtails.taeda.domain.service.general.CollectionService
import foxtails.taeda.domain.service.general.PostService
import foxtails.taeda.domain.service.general.Session
import foxtails.taeda.domain.service.platform.Platform
import foxtails.taeda.domain.service.preferences.UserPreferences
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.ui.composables.profile.AccountState
import foxtails.taeda.ui.composables.profile.CollectionsState
import foxtails.taeda.ui.composables.profile.PostsState
import foxtails.taeda.ui.composables.profile.ViewEnum
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.getPluralString
import foxtails.taeda.app.generated.resources.Res
import foxtails.taeda.app.generated.resources.follower
import foxtails.taeda.app.generated.resources.following
import foxtails.taeda.app.generated.resources.posts

class OwnProfileViewModel @Inject constructor(
    private val accountService: AccountService,
    private val postService: PostService,
    private val prefs: UserPreferences,
    private val collectionService: CollectionService,
    private val authService: AuthService,
    private val platform: Platform,
    appIconService: AppIconService,
    session: Session
) : ViewModel() {
    val capabilities = session.capabilities
    val backendType: BackendType = session.backendType.value
    var accountState by mutableStateOf(AccountState())
    var postsState by mutableStateOf(PostsState())
    var ownDomain by mutableStateOf("")
    var view by mutableStateOf(ViewEnum.Grid)
    private var collectionPage by mutableIntStateOf(1)
    val appIcon = appIconService.currentIcon

    var collectionsState by mutableStateOf(CollectionsState())

    var postsLabel by mutableStateOf("")
    var followerLabel by mutableStateOf("")
    var followingLabel by mutableStateOf("")

    init {
        loadData(false)

        viewModelScope.launch {
            prefs.showUserGridTimelineFlow.collect { res ->
                view = ViewEnum.getView(res)
            }
        }
        ownDomain = authService.getCurrentSession()?.serverUrl.orEmpty()
    }

    private fun resolvePluralLabels(account: Account) {
        viewModelScope.launch {
            postsLabel = getPluralString(Res.plurals.posts, account.postsCount, account.postsCount)
            followerLabel = getPluralString(
                Res.plurals.follower,
                account.followersCount,
                account.followersCount
            )
            followingLabel = getPluralString(
                Res.plurals.following,
                account.followingCount,
                account.followingCount
            )
        }
    }

    fun dismissError() {
        accountState = accountState.copy(error = "")
    }

    fun updateAccountSwitch() {
        loadData(false)
        ownDomain = authService.getCurrentSession()?.serverUrl.orEmpty()
    }

    fun loadData(refreshing: Boolean) {
        getAccount(refreshing)
        getPostsFirstLoad(refreshing)

        if (capabilities.value.profile.showCollectionsOwnProfile) {
            viewModelScope.launch {
                val currentLoginData = authService.getCurrentSession()
                currentLoginData?.let {
                    collectionsState = collectionsState.copy(endReached = false)
                    getCollections(it.accountId, false)
                }
            }
        }
    }

    private fun getAccount(refreshing: Boolean) {
        accountService.getOwnAccount().onEach { result ->
            accountState = when (result) {
                is Resource.Success -> {
                    resolvePluralLabels(result.data)
                    AccountState(account = result.data)
                }

                is Resource.Error -> {
                    AccountState(error = result.message)
                }

                is Resource.Loading -> {
                    accountState.copy(isLoading = true, refreshing = refreshing)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getPostsFirstLoad(refreshing: Boolean) {
        if (postsState.posts.isNotEmpty() && !refreshing) {
            return
        }
        postService.getOwnPosts().onEach { result ->
            postsState = when (result) {
                is Resource.Success -> {
                    val endReached = (result.data.data.size) < PixelfedApi.PROFILE_POSTS_LIMIT
                    PostsState(posts = result.data.data, endReached = endReached, nextId = result.data.next)
                }

                is Resource.Error -> {
                    PostsState(error = result.message)
                }

                is Resource.Loading -> {
                    PostsState(isLoading = true, posts = postsState.posts, refreshing = refreshing, nextId = postsState.nextId)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getPostsPaginated() {
        if (postsState.posts.isNotEmpty() && !postsState.isLoading && !postsState.endReached) {
            postService.getOwnPosts(postsState.nextId).onEach { result ->
                postsState = when (result) {
                    is Resource.Success -> {
                        val endReached = result.data.data.size < PixelfedApi.PROFILE_POSTS_LIMIT
                        PostsState(
                            posts = postsState.posts + (result.data.data),
                            endReached = endReached,
                            nextId = result.data.next
                        )
                    }

                    is Resource.Error -> {
                        PostsState(error = result.message)
                    }

                    is Resource.Loading -> {
                        PostsState(isLoading = true, posts = postsState.posts, nextId = postsState.nextId)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getCollections(userId: String, paginated: Boolean) {
        if (collectionsState.endReached) {
            return
        }
        if (!paginated) {
            collectionPage = 1
        } else {
            collectionPage++
        }
        collectionService.getCollections(userId, collectionPage).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    collectionsState = if (!paginated) {
                        CollectionsState(collections = result.data)
                    } else {
                        val endReached = result.data.isEmpty()
                        CollectionsState(
                            collections = collectionsState.collections + result.data,
                            endReached = endReached
                        )
                    }
                }

                is Resource.Error -> {
                    collectionsState =
                        CollectionsState(error = result.message)
                }

                is Resource.Loading -> {
                    collectionsState = CollectionsState(
                        isLoading = true, collections = collectionsState.collections
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun openUrl(url: String) {
        platform.openUrl(url)
    }

    fun changeView(newView: ViewEnum) {
        view = newView
        prefs.showUserGridTimeline = newView.ordinal
    }

    fun postGetsDeleted(postId: String) {
        postsState = postsState.copy(posts = postsState.posts.filter { post -> post.id != postId })
        accountState = accountState.copy(
            account = accountState.account?.copy(
                postsCount = accountState.account?.postsCount?.minus(
                    1
                )
                    ?: 0
            )
        )
    }

    fun updatePost(post: Post) {
        postsState = postsState.copy(posts = postsState.posts.map {
            if (it.id == post.id) {
                post
            } else {
                it
            }
        })
    }
}