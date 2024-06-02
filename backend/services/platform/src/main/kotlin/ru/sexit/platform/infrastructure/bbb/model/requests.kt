package ru.sexit.platform.infrastructure.bbb.model

import ru.sexit.platform.config.properties.BbbProps
import java.util.UUID

data class BbbCreateMeetingRequest(
    val name: String,
    val meetingId: UUID,
) {
    fun toParamsMap(bbbProps: BbbProps): Map<String, String> = mapOf(
        "name" to name,
        "meetingID" to meetingId.toString(),
//        "logoutURL" to bbbProps.logoutUrl, // TODO: fix this kakaha
        "duration" to bbbProps.durationInMinutes.toString()
    )
}

enum class BbbUserRole {
    MODERATOR,
    VIEWER,
    ;
}

data class BbbJoinMeetingRequest(
    val userId: Long,
    val userFullName: String,
    val meetingId: UUID,
    val userRole: BbbUserRole,
) {
    fun toParamsMap(bbbProps: BbbProps): Map<String, String> = mapOf(
        "fullName" to userFullName,
        "meetingID" to meetingId.toString(),
        "role" to userRole.name,
        "errorRedirectUrl" to bbbProps.errorRedirectUrl,
    )
}
