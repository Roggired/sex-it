package ru.sexit.platform.infrastructure.bbb.client

import org.apache.commons.codec.digest.DigestUtils
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import ru.sexit.platform.infrastructure.BbbIntegrationException
import ru.sexit.platform.infrastructure.bbb.model.BbbCreateMeetingRequest
import ru.sexit.platform.infrastructure.bbb.model.BbbGeneralResponse
import ru.sexit.platform.infrastructure.bbb.model.BbbJoinMeetingRequest
import ru.sexit.platform.infrastructure.bbb.model.BbbJoinMeetingResponse
import ru.sexit.platform.utils.log
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

enum class BbbClientAction(
    val value: String
) {
    CREATE("create"),
    JOIN("join"),
    ;
}

interface BbbClient {
    fun createMeeting(request: BbbCreateMeetingRequest)
    fun joinMeeting(request: BbbJoinMeetingRequest): BbbJoinMeetingResponse

    fun bbbRequestBuilder(
        url: String,
        action: BbbClientAction,
        secret: String,
        params: Map<String, String>
    ): String {
        log.info("Secret: $secret")
        val paramsAsStringUrlEncoded = params.entries.joinToString(separator = "&") { "${it.key}=${URLEncoder.encode(it.value, StandardCharsets.UTF_8)}" }
        val paramsAsString = params.entries.joinToString(separator = "&") { "${it.key}=${it.value}" }
        val checksumBase = "${action.value}$paramsAsStringUrlEncoded$secret"

        val checksum = DigestUtils.sha1Hex(checksumBase)

        return "$url/api/${action.value}?$paramsAsStringUrlEncoded&checksum=$checksum"
    }

    fun bbbRestTemplate(
        evaluatedRequest: String
    ) {
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.APPLICATION_XML)
        val bbbResponseEntity = RestTemplate().postForEntity(
            evaluatedRequest,
            HttpEntity<Any>(headers),
            BbbGeneralResponse::class.java
        )

        if (bbbResponseEntity.statusCode != HttpStatus.OK) {
            throw BbbIntegrationException("Status code: ${bbbResponseEntity.statusCode}.")
        }

        val bbbGeneralResponse = bbbResponseEntity.body!!
        if (bbbGeneralResponse.returncode?.trim()?.uppercase() == "FAILED") {
            throw BbbIntegrationException("Status code: ${bbbResponseEntity.statusCode}, but returnCode of BBB response: ${bbbGeneralResponse.returncode}. MessageKey: ${bbbGeneralResponse.messageKey}")
        }
    }
}
