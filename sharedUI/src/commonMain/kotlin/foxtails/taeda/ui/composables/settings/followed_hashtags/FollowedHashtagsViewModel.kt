package foxtails.taeda.ui.composables.settings.followed_hashtags

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.model.Tag
import foxtails.taeda.domain.service.general.ExploreService
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class FollowedHashtagsViewModel @Inject constructor(
    private val exploreService: ExploreService
) : ViewModel() {

    var followedHashtagsState by mutableStateOf(FollowedHashtagsState())

    init {
        getFollowedHashtags()
    }

    fun getFollowedHashtags(refreshing: Boolean = false) {
        exploreService.getFollowedHashtags().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    followedHashtagsState = FollowedHashtagsState(followedHashtags = result.data)
                }

                is Resource.Error -> {
                    followedHashtagsState = FollowedHashtagsState(
                        error = result.message
                    )
                }

                is Resource.Loading -> {
                    followedHashtagsState = FollowedHashtagsState(
                        isLoading = true,
                        isRefreshing = refreshing,
                        followedHashtags = followedHashtagsState.followedHashtags
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}