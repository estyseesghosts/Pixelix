package foxtails.taeda.domain.service.pixelfed

import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.general.InstanceService
import foxtails.taeda.domain.service.pixelfed.model.toDomain
import foxtails.taeda.domain.service.utils.loadResource
import me.tatarka.inject.annotations.Inject

@Inject
class PixelfedInstanceService(
    private val api: PixelfedApi
): InstanceService {

    override fun getInstance() = loadResource {
        api.getInstance().toDomain()
    }

    override fun getNodeInfo(domain: String) = loadResource {
        api.getNodeInfo(domain).toDomain()
    }
}