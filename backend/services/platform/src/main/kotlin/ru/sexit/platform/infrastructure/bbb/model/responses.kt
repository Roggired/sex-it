package ru.sexit.platform.infrastructure.bbb.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

data class BbbJoinMeetingResponse(
    val redirectUrl: String,
)

@JacksonXmlRootElement(localName = "response")
data class BbbGeneralResponse(
    val returncode: String?,
    val messageKey: String?,
    val message: String?,
)
