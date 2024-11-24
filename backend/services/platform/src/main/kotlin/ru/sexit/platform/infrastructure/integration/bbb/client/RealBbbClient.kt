package ru.sexit.platform.infrastructure.integration.bbb.client

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import ru.sexit.platform.config.properties.BbbProps
import ru.sexit.platform.infrastructure.integration.bbb.model.BbbCreateMeetingRequest
import ru.sexit.platform.infrastructure.integration.bbb.model.BbbJoinMeetingRequest
import ru.sexit.platform.infrastructure.integration.bbb.model.BbbJoinMeetingResponse
import ru.sexit.platform.utils.log

@Service
@ConditionalOnProperty(value = ["bbb.client-mode"], havingValue = "real")
class RealBbbClient(
    private val bbbProps: BbbProps,
): BbbClient {
    override fun createMeeting(request: BbbCreateMeetingRequest) {
        val evaluatedRequest = bbbRequestBuilder(
            url = bbbProps.url,
            action = BbbClientAction.CREATE,
            secret = bbbProps.secret,
            params = request.toParamsMap(bbbProps),
        )

        bbbRestTemplate(evaluatedRequest)

        log.info("Successfully created meeting with id: ${request.meetingId}")
    }

    override fun joinMeeting(request: BbbJoinMeetingRequest): BbbJoinMeetingResponse {
        val evaluatedRequest = bbbRequestBuilder(
            url = bbbProps.url,
            action = BbbClientAction.JOIN,
            secret = bbbProps.secret,
            params = request.toParamsMap(bbbProps),
        )

        log.info("Successfully generated JOIN url for meeting id: ${request.meetingId} and user id: ${request.userId}")

        return BbbJoinMeetingResponse(
            redirectUrl = evaluatedRequest,
        )
    }
}
