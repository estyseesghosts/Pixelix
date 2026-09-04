package com.daniebeler.pfpixelix.domain.service.sharkey

import androidx.compose.ui.graphics.ImageBitmap
import com.daniebeler.pfpixelix.domain.model.Account
import com.daniebeler.pfpixelix.domain.model.MutedAccount
import com.daniebeler.pfpixelix.domain.model.PaginatedResponse
import com.daniebeler.pfpixelix.domain.model.Relationship
import com.daniebeler.pfpixelix.domain.model.Settings
import com.daniebeler.pfpixelix.domain.model.request.UpdateUserRequest
import com.daniebeler.pfpixelix.domain.model.request.UserBlockRequest
import com.daniebeler.pfpixelix.domain.model.request.UserMuteRequest
import com.daniebeler.pfpixelix.domain.repository.sharkey.SharkeyApi
import com.daniebeler.pfpixelix.domain.service.general.AccountService
import com.daniebeler.pfpixelix.domain.service.general.AuthService
import com.daniebeler.pfpixelix.domain.service.general.Session
import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyRequest
import com.daniebeler.pfpixelix.domain.service.sharkey.model.toDomain
import com.daniebeler.pfpixelix.domain.service.utils.Resource
import com.daniebeler.pfpixelix.domain.service.utils.loadListResources
import com.daniebeler.pfpixelix.domain.service.utils.loadPaginatedListResources
import com.daniebeler.pfpixelix.domain.service.utils.loadResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import me.tatarka.inject.annotations.Inject

@Inject
class SharkeyAccountService(
    private val api: SharkeyApi,
    private val session: Session,
    private val authService: AuthService
) : AccountService {
    override val refreshSignal = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOwnAccount(): Flow<Resource<Account>> {
        val current = authService.getCurrentSession() ?: return flowOf(Resource.Error("No account found"))
        return refreshSignal.onStart { emit(Unit) }.flatMapLatest {
            getAccount(current.accountId, current.username)
        }
    }

    override fun updateAccount(username: String, updateUserRequest: UpdateUserRequest): Flow<Resource<Account>> = unsupported()

    override fun updateAvatar(username: String, avatar: ImageBitmap?): Flow<Resource<Unit>> = unsupported()

    override fun updateHeader(username: String, header: ImageBitmap?): Flow<Resource<Unit>> = unsupported()

    override fun getAccount(accountId: String, username: String) = loadResource {
        api.getUser(request(userId = accountId)).toDomain()
    }

    override fun getAccountByUsername(username: String) = loadResource {
        val normalized = username.trim().removePrefix("@")
        val localUsername = normalized.substringBefore("@")
        val host = normalized.substringAfter("@", "")
        api.searchUsers(request(query = normalized, limit = 20, detail = true))
            .firstOrNull { candidate ->
                val candidateUsername = candidate.username.orEmpty()
                val candidateIdentity = if (candidate.host.isNullOrBlank()) {
                    candidateUsername
                } else {
                    "$candidateUsername@${candidate.host}"
                }
                candidateIdentity.equals(normalized, ignoreCase = true) ||
                    (host.isEmpty() && candidateUsername.equals(localUsername, ignoreCase = true))
            }
            ?.toDomain() ?: error("Sharkey user '$username' was not found.")
    }

    override fun getRelationships(userIds: List<String>) = loadListResources {
        api.getRelationships(request(userIds = userIds)).map { relation ->
            Relationship(
                id = relation.userId.orEmpty(),
                following = relation.isFollowing ?: false,
                followedBy = relation.isFollowed ?: false,
                blocked = relation.isBlocking ?: false,
                muted = relation.isMuted ?: false,
                requested = false,
                requestedBy = false
            )
        }
    }

    override fun getMutualFollowers(userId: String): Flow<Resource<List<Account>>> = unsupported()

    override fun getAccountSettings(): Flow<Resource<Settings>> = unsupported()

    override fun followAccount(accountId: String, username: String) = loadResource {
        api.follow(request(userId = accountId))
        relation(accountId, following = true)
    }

    override fun unfollowAccount(accountId: String, username: String) = loadResource {
        api.unfollow(request(userId = accountId))
        relation(accountId, following = false)
    }

    override fun muteAccount(
        accountId: String,
        username: String,
        userMuteRequest: UserMuteRequest
    ): Flow<Resource<Relationship>> = unsupported()

    override fun blockAccount(
        accountId: String,
        username: String,
        userBlockRequest: UserBlockRequest
    ): Flow<Resource<Relationship>> = unsupported()

    override fun unblockAccount(accountId: String, username: String): Flow<Resource<Relationship>> = unsupported()

    override fun getMutedAccounts(): Flow<Resource<List<MutedAccount>>> = unsupported()

    override fun getBlockedAccounts(): Flow<Resource<List<Account>>> = unsupported()

    override fun getAccountsFollowers(accountId: String, username: String, cursor: String?) =
        loadPaginatedListResources {
            api.getFollowers(request(userId = accountId, untilId = cursor)).map { it.toDomain() }
        }

    override fun getAccountsFollowing(accountId: String, username: String, cursor: String?) =
        loadPaginatedListResources {
            api.getFollowing(request(userId = accountId, untilId = cursor)).map { it.toDomain() }
        }

    override fun acceptFollowRequest(accountId: String): Flow<Resource<Relationship>> = unsupported()

    override fun rejectFollowRequest(accountId: String): Flow<Resource<Relationship>> = unsupported()

    private suspend fun relation(accountId: String, following: Boolean): Relationship {
        val relation = api.getRelationships(request(userIds = listOf(accountId))).firstOrNull()
        return Relationship(
            id = accountId,
            following = relation?.isFollowing ?: following,
            followedBy = relation?.isFollowed ?: false,
            blocked = relation?.isBlocking ?: false,
            muted = relation?.isMuted ?: false,
            requested = false,
            requestedBy = false
        )
    }

    private fun request(
        userId: String? = null,
        userIds: List<String>? = null,
        untilId: String? = null,
        limit: Int? = null,
        query: String? = null,
        detail: Boolean? = null
    ) = SharkeyRequest(
        i = requireNotNull(session.credentials.value?.token) { "No Sharkey session found." },
        userId = userId,
        userIds = userIds,
        untilId = untilId,
        limit = limit,
        query = query,
        detail = detail
    )

    private fun <T> unsupported(): Flow<Resource<T>> =
        flowOf(Resource.Error("This account action is not supported by Sharkey."))
}