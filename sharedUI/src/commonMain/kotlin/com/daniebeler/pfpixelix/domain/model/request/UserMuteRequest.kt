package com.daniebeler.pfpixelix.domain.model.request


data class UserMuteRequest(
    val mute: Boolean? = null,
    val muteStatuses: Boolean? = null,
    val muteReblogs: Boolean? = null,
    val muteNotifications: Boolean? = null,
    val removeStatusesFromTimeline: Boolean? = null,
    val removeReblogsFromTimeline: Boolean? = null,
    val endDate: kotlin.time.Instant? = null
)
