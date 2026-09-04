package foxtails.taeda.ui.composables.post_editor

import foxtails.taeda.domain.model.License

data class LicensesState(
    var isLoading: Boolean = false,
    var error: String = "",
    var selectedLicense: License? = null,
    var licenses: List<License> = emptyList()
)
