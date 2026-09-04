package foxtails.taeda.domain.service.general

import foxtails.taeda.di.AppSingleton
import foxtails.taeda.domain.model.MediaAttachment
import foxtails.taeda.domain.model.Post
import foxtails.taeda.domain.model.request.MediaAttachmentMetadataRequest
import foxtails.taeda.domain.model.request.NewPostRequest
import foxtails.taeda.domain.service.pixelfed.PixelfedPostEditorService
import foxtails.taeda.domain.service.utils.Resource
import foxtails.taeda.utils.KmpUri
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

interface PostEditorService {

    fun uploadMedia(uri: KmpUri): Flow<Resource<MediaAttachment>>

    fun updateMedia(id: String, metadata: MediaAttachmentMetadataRequest): Flow<Resource<Unit>>

    fun createPost(createPostDto: NewPostRequest): Flow<Resource<Post>>

    fun updatePost(postId: String, updatePostDto: NewPostRequest): Flow<Resource<Unit>>

    fun deletePost(postId: String): Flow<Resource<Unit>>
}

@Inject
@AppSingleton
class PostEditorServiceDelegate(
    private val session: Session,
    private val pixelfed: PixelfedPostEditorService,
) : PostEditorService {

    private val current: PostEditorService
        get() = when (session.backendType.value) {
            else -> pixelfed
        }

    override fun uploadMedia(
        uri: KmpUri
    ): Flow<Resource<MediaAttachment>> = current.uploadMedia(uri)

    override fun updateMedia(
        id: String, metadata: MediaAttachmentMetadataRequest
    ) = current.updateMedia(id, metadata)

    override fun createPost(createPostDto: NewPostRequest): Flow<Resource<Post>> =
        current.createPost(createPostDto)

    override fun updatePost(
        postId: String, updatePostDto: NewPostRequest
    ): Flow<Resource<Unit>> = current.updatePost(postId, updatePostDto)

    override fun deletePost(postId: String): Flow<Resource<Unit>> = current.deletePost(postId)

}