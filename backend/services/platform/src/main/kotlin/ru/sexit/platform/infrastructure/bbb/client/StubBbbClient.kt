package ru.sexit.platform.infrastructure.bbb.client

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import ru.sexit.platform.config.properties.BbbProps
import ru.sexit.platform.infrastructure.bbb.model.BbbCreateMeetingRequest
import ru.sexit.platform.infrastructure.bbb.model.BbbJoinMeetingRequest
import ru.sexit.platform.infrastructure.bbb.model.BbbJoinMeetingResponse
import ru.sexit.platform.utils.log

@Service
@ConditionalOnProperty(value = ["bbb.client-mode"], havingValue = "stub", matchIfMissing = true)
class StubBbbClient(
    private val bbbProps: BbbProps,
): BbbClient {
    override fun createMeeting(request: BbbCreateMeetingRequest) {
        val evaluatedRequest = bbbRequestBuilder(
            url = bbbProps.url,
            action = BbbClientAction.CREATE,
            secret = bbbProps.secret,
            params = request.toParamsMap(bbbProps),
        )

        log.info("BBB CREATE Request stub has been evaluated: $evaluatedRequest")
    }

    override fun joinMeeting(request: BbbJoinMeetingRequest): BbbJoinMeetingResponse {
        val evaluatedRequest = bbbRequestBuilder(
            url = bbbProps.url,
            action = BbbClientAction.JOIN,
            secret = bbbProps.secret,
            params = request.toParamsMap(bbbProps),
        )

        log.info("BBB JOIN Request stub has been evaluated: $evaluatedRequest")

        return BbbJoinMeetingResponse(
            redirectUrl = evaluatedRequest,
        )
    }
}
