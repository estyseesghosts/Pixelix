package foxtails.taeda.ui.composables.profile

import foxtails.taeda.domain.model.FediseaInstance
import foxtails.taeda.domain.model.FediseaSoftware

data class DomainSoftwareState(
    val isLoading: Boolean = false,
    val fediSoftware: FediseaSoftware? = null,
    val fediServer: FediseaInstance? = null,
    val error: String = ""
)
