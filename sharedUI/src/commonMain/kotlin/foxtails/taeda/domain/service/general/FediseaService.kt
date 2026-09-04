package foxtails.taeda.domain.service.general

import foxtails.taeda.domain.model.FediseaInstance
import foxtails.taeda.domain.model.FediseaServersResponse
import foxtails.taeda.domain.model.FediseaSoftware
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.service.utils.loadResource
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class FediseaService(
    private val api: PixelfedApi
) {

    fun getSoftwareFromFedisea(slug: String): Flow<Resource<FediseaSoftware>> = loadResource {
        api.getSoftwareFromFedisea(slug)
    }

    fun getServerFromFedisea(slug: String): Flow<Resource<FediseaInstance>> = loadResource {
        api.getServerFromFedisea(domain = slug)
    }

    fun getOpenServers(
        search: String, backendType: BackendType, limit: Int
    ): Flow<Resource<FediseaServersResponse>> = loadResource {
        val softwareName = when (backendType) {
            BackendType.MASTODON -> "mastodon"
            BackendType.SHARKEY -> "sharkey"
        }
        api.getOpenServers(search, software = softwareName, size = limit)
    }
}