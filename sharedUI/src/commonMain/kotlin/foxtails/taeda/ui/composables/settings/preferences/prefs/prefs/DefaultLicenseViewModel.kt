package foxtails.taeda.ui.composables.settings.preferences.prefs.prefs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import foxtails.taeda.domain.model.License
import foxtails.taeda.domain.service.general.ExploreService
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.ui.composables.profile.ViewEnum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

@Inject
class DefaultLicenseViewModel(
    private val exploreService: ExploreService
) : ViewModel() {
    private val _licenses = MutableStateFlow<List<License>>(emptyList())

    val licenses: StateFlow<List<License>> = _licenses.asStateFlow()

    var isLoading by mutableStateOf(false)

    init {
        getLicenses()
    }


    private fun getLicenses() {
        exploreService.getLicenses().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _licenses.value = result.data
                    isLoading = false
                }

                is Resource.Error -> {
                }

                is Resource.Loading -> {
                    isLoading = true
                }
            }
        }.launchIn(viewModelScope)
    }
}