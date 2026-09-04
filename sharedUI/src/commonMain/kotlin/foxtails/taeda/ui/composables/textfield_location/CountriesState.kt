package foxtails.taeda.ui.composables.textfield_location

import foxtails.taeda.domain.model.Country

data class CountriesState(
    val isLoading: Boolean = false,
    val countries: List<Country> = emptyList(),
    val filteredCountries: List<Country> = countries,
    val country: Country? = null,
    val error: String = ""
)
