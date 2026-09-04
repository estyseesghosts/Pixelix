package foxtails.taeda.domain.service.pixelfed

import foxtails.taeda.domain.model.NewMessage
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.general.DirectMessagesService
import foxtails.taeda.domain.service.pixelfed.model.toDomain
import foxtails.taeda.domain.service.utils.loadListResources
import foxtails.taeda.domain.service.utils.loadResource
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class PixelfedDirectMessagesService(
    private val api: PixelfedApi,
    private val json: Json
): DirectMessagesService {
    override fun getConversations() = loadListResources {
        api.getConversations().map { it.toDomain() }
    }

    override fun getChat(accountId: String, maxId: String?) = loadResource {
        api.getChat(accountId, maxId).toDomain()
    }

    override fun sendMessage(createMessageDto: NewMessage) = loadResource {
        api.sendMessage(createMessageDto).toDomain()
    }

    override fun deleteMessage(id: String) = loadResource {
        api.deleteMessage(id)
    }
}