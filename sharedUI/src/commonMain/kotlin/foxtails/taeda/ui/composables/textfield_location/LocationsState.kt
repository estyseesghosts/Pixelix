package foxtails.taeda.ui.composables.textfield_location

import foxtails.taeda.domain.model.Location

data class LocationsState(
    val isLoading: Boolean = false,
    val locations: List<Location> = emptyList(),
    val location: Location? = null,
    val error: String = ""
)
