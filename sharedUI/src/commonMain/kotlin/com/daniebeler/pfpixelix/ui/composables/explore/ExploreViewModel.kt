package com.daniebeler.pfpixelix.ui.composables.explore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniebeler.pfpixelix.domain.service.utils.Resource
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.SavedSearchItem
import com.daniebeler.pfpixelix.domain.service.capabilities.Capabilities
import com.daniebeler.pfpixelix.domain.service.general.AuthService
import com.daniebeler.pfpixelix.domain.service.general.ExploreService
import com.daniebeler.pfpixelix.domain.service.general.Session
import com.daniebeler.pfpixelix.domain.service.preferences.UserPreferences
import com.daniebeler.pfpixelix.domain.service.search.SavedSearchesService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class ExploreViewModel @Inject constructor(
    private val exploreService: ExploreService,
    private val savedSearchesService: SavedSearchesService,
    private val authService: AuthService,
    private val prefs: UserPreferences,
    session: Session
) : ViewModel() {
    val capabilities: StateFlow<Capabilities> = session.capabilities
    var searchState by mutableStateOf(SearchState())
    var savedSearches by mutableStateOf<List<SavedSearchItem>>(emptyList())
        private set

    var isSwipeEnabled by mutableStateOf(true)

    init {
        viewModelScope.launch {
            // 1. Observe the active user flow
            authService.activeUser.collect { accountId ->
                if (accountId != null) {
                    savedSearchesService.getSavedSearches(accountId).collect { list ->
                        savedSearches = list
                    }
                } else {
                    savedSearches = emptyList()
                }
            }
        }
        viewModelScope.launch {
            prefs.enableSwipeBetweenTabsFlow.collect { isSwipeEnabled = it }
        }
    }

    fun saveAccount(accountUsername: String, account: Account) {
        viewModelScope.launch {
            val currentUserId = authService.getCurrentSession()?.accountId ?: return@launch
            savedSearchesService.addAccount(accountUsername, account, currentUserId)
        }
    }

    fun saveHashtag(accountId: String) {
        viewModelScope.launch {
            val currentUserId = authService.getCurrentSession()?.accountId ?: return@launch

            savedSearchesService.addHashtag(accountId, currentUserId)
        }
    }

    fun saveSearch(text: String) {
        if (text.isNotBlank()) {

            /*val savedSearchesBefore =
                savedSearches.pastSearches.filter { it.savedSearchType == SavedSearchType.Search }
            if (savedSearchesBefore.find { it.value == text } != null) {
                return
            }*/

            viewModelScope.launch {
                val currentUserId = authService.getCurrentSession()?.accountId ?: return@launch

                savedSearchesService.addSearch(text, currentUserId)
            }
        }
    }

    fun deleteSavedSearch(item: SavedSearchItem) {
        viewModelScope.launch {
            val currentUserId = authService.getCurrentSession()?.accountId ?: return@launch

            savedSearchesService.deleteElement(item, currentUserId)
        }
    }

    fun onSearch(text: String) {
        if (text.isBlank()) {
            textInputChange(text)
            return
        }
        searchJob?.cancel()
        skipDebounceForText = text
        searchState = SearchState(isLoading = true)
        searchJob = search(text, 20)
    }

    fun textInputChange(text: String) {
        if (skipDebounceForText == text) {
            skipDebounceForText = null
            return
        }
        searchJob?.cancel()
        if (text.isBlank()) {
            searchState = SearchState()
            searchJob = null
            return
        }
        searchState = SearchState(isLoading = true)
        searchJob = search(text, 5, debounce = true)
    }

    private var searchJob: Job? = null
    private var skipDebounceForText: String? = null

    private fun search(text: String, limit: Int, debounce: Boolean = false): Job = viewModelScope.launch {
        if (debounce) delay(500)
        if (text.isBlank()) {
            searchState = SearchState()
            return@launch
        }
        exploreService.search(text, limit = limit).collect { result ->
            searchState = when (result) {
                is Resource.Success -> SearchState(searchResult = result.data)
                is Resource.Error -> SearchState(error = result.message ?: "An unexpected error occurred")
                is Resource.Loading -> SearchState(isLoading = true)
            }
        }
    }
}