package foxtails.taeda.domain.service.general

import foxtails.taeda.di.AppSingleton
import foxtails.taeda.domain.model.FediseaInstance
import foxtails.taeda.domain.model.FediseaServersResponse
import foxtails.taeda.domain.model.FediseaSoftware
import foxtails.taeda.domain.model.Instance
import foxtails.taeda.domain.model.NodeInfo
import foxtails.taeda.domain.service.pixelfed.PixelfedExploreService
import foxtails.taeda.domain.service.pixelfed.PixelfedInstanceService
import foxtails.taeda.domain.service.utils.Resource
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

interface InstanceService {

    fun getInstance(): Flow<Resource<Instance>>

    fun getNodeInfo(domain: String): Flow<Resource<NodeInfo>>
}

@Inject
@AppSingleton
class InstanceServiceDelegate(
    private val session: Session,
    private val pixelfed: PixelfedInstanceService,
) : InstanceService {

    private val current: InstanceService
        get() = when (session.backendType.value) {
            else -> pixelfed
        }

    override fun getInstance(): Flow<Resource<Instance>> = current.getInstance()

    override fun getNodeInfo(domain: String): Flow<Resource<NodeInfo>> = current.getNodeInfo(domain)
}