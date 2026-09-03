package com.daniebeler.pfpixelix.domain.repository.sharkey

import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyNoteDto
import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyNotificationDto
import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyRelationshipDto
import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyRequest
import com.daniebeler.pfpixelix.domain.service.sharkey.model.SharkeyUserDto
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface SharkeyApi {
    @Headers("Content-Type: application/json")
    @POST("api/notes/timeline")
    suspend fun getHomeTimeline(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/notes/local-timeline")
    suspend fun getLocalTimeline(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/notes/global-timeline")
    suspend fun getGlobalTimeline(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/notes/search-by-tag")
    suspend fun getHashtagTimeline(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/notes/featured")
    suspend fun getFeaturedNotes(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/notifications")
    suspend fun getNotifications(@Body request: SharkeyRequest): List<SharkeyNotificationDto>

    @Headers("Content-Type: application/json")
    @POST("api/notifications/mark-as-read")
    suspend fun markNotificationAsRead(@Body request: SharkeyRequest)

    @Headers("Content-Type: application/json")
    @POST("api/notes/search")
    suspend fun searchNotes(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/notes/show")
    suspend fun getNote(@Body request: SharkeyRequest): SharkeyNoteDto

    @Headers("Content-Type: application/json")
    @POST("api/notes/favorites")
    suspend fun getFavorites(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/notes/favorites/create")
    suspend fun favorite(@Body request: SharkeyRequest)

    @Headers("Content-Type: application/json")
    @POST("api/notes/favorites/delete")
    suspend fun unfavorite(@Body request: SharkeyRequest)

    @Headers("Content-Type: application/json")
    @POST("api/notes/create")
    suspend fun createNote(@Body request: SharkeyRequest): SharkeyNoteDto

    @Headers("Content-Type: application/json")
    @POST("api/notes/mentions")
    suspend fun getMentionedNotes(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/notes/delete")
    suspend fun deleteNote(@Body request: SharkeyRequest)

    @Headers("Content-Type: application/json")
    @POST("api/users/show")
    suspend fun getUser(@Body request: SharkeyRequest): SharkeyUserDto

    @Headers("Content-Type: application/json")
    @POST("api/users/search")
    suspend fun searchUsers(@Body request: SharkeyRequest): List<SharkeyUserDto>

    @Headers("Content-Type: application/json")
    @POST("api/users/notes")
    suspend fun getUserNotes(@Body request: SharkeyRequest): List<SharkeyNoteDto>

    @Headers("Content-Type: application/json")
    @POST("api/users/relation")
    suspend fun getRelationships(@Body request: SharkeyRequest): List<SharkeyRelationshipDto>

    @Headers("Content-Type: application/json")
    @POST("api/following/create")
    suspend fun follow(@Body request: SharkeyRequest)

    @Headers("Content-Type: application/json")
    @POST("api/following/delete")
    suspend fun unfollow(@Body request: SharkeyRequest)

    @Headers("Content-Type: application/json")
    @POST("api/users/followers")
    suspend fun getFollowers(@Body request: SharkeyRequest): List<SharkeyUserDto>

    @Headers("Content-Type: application/json")
    @POST("api/users/following")
    suspend fun getFollowing(@Body request: SharkeyRequest): List<SharkeyUserDto>

}