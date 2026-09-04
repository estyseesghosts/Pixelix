package foxtails.taeda.ui.composables.settings.about_instance

import foxtails.taeda.domain.model.Instance

data class InstanceState(
    val isLoading: Boolean = false,
    val instance: Instance? = null,
    val error: String = ""
)
