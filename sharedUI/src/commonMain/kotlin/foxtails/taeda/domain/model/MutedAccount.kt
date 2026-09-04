package foxtails.taeda.domain.model

import foxtails.taeda.domain.model.request.UserMuteRequest

data class MutedAccount(
    override val id: String = "",
    val account: Account,
    val muteOptions: UserMuteRequest
): Identifiable

