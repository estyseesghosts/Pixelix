package foxtails.taeda.domain.service.pixelfed

import foxtails.taeda.domain.repository.pixelfed.PixelfedApi
import foxtails.taeda.domain.service.general.CollectionService
import foxtails.taeda.domain.service.pixelfed.model.toDomain
import foxtails.taeda.domain.service.utils.loadListResources
import foxtails.taeda.domain.service.utils.loadResource
import me.tatarka.inject.annotations.Inject

@Inject
class PixelfedCollectionService(
    private val api: PixelfedApi
): CollectionService {

    override fun getCollections(userId: String, page: Int) = loadListResources {
        api.getCollectionsByUserId(userId, page).map { it.toDomain() }
    }

    override fun getCollection(collectionId: String) = loadResource {
        api.getCollection(collectionId).toDomain()
    }

    override fun getPostsOfCollection(collectionId: String, page: Int) = loadListResources {
        api.getPostsOfCollection(collectionId, page).map { it.toDomain() }
    }

    override fun removePostOfCollection(collectionId: String, postId: String) = loadResource {
        api.removePostOfCollection(collectionId, postId)
    }

    override fun addPostOfCollection(collectionId: String, postId: String) = loadResource {
        api.addPostOfCollection(collectionId, postId)
    }

    override fun updateCollection(
        collectionId: String,
        title: String,
        description: String,
        visibility: String
    ) = loadResource {
        api.updateCollection(collectionId, title, description, visibility).toDomain()
    }
}