package foxtails.taeda.domain.service.general

import foxtails.taeda.di.AppSingleton
import foxtails.taeda.domain.model.Chat
import foxtails.taeda.domain.model.Conversation
import foxtails.taeda.domain.model.Message
import foxtails.taeda.domain.model.NewMessage
import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.pixelfed.PixelfedCollectionService
import foxtails.taeda.domain.service.pixelfed.PixelfedDirectMessagesService
import foxtails.taeda.domain.service.sharkey.SharkeyDirectMessagesService
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.domain.service.utils.loadListResources
import foxtails.taeda.domain.service.utils.loadResource
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

interface DirectMessagesService {
    fun getConversations(): Flow<Resource<List<Conversation>>>

    fun getChat(accountId: String, maxId: String? = null): Flow<Resource<Chat>>

    fun sendMessage(createMessageDto: NewMessage): Flow<Resource<Message>>

    fun deleteMessage(id: String): Flow<Resource<List<Int>>>
}

@Inject
@AppSingleton
class DirectMessagesServiceDelegate(
    private val session: Session,
    private val pixelfed: PixelfedDirectMessagesService,
    private val sharkey: SharkeyDirectMessagesService,
) : DirectMessagesService {

    private val current: DirectMessagesService
        get() = when (session.backendType.value) {
            BackendType.SHARKEY -> sharkey
            else -> pixelfed
        }

    override fun getConversations(): Flow<Resource<List<Conversation>>> = current.getConversations()

    override fun getChat(
        accountId: String,
        maxId: String?
    ): Flow<Resource<Chat>> = current.getChat(accountId, maxId)

    override fun sendMessage(createMessageDto: NewMessage): Flow<Resource<Message>> = current.sendMessage(createMessageDto)

    override fun deleteMessage(id: String): Flow<Resource<List<Int>>> = current.deleteMessage(id)
}