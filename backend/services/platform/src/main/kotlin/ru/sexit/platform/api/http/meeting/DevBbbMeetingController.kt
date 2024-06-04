package ru.sexit.platform.api.http.meeting

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sexit.platform.domain.model.BbbMeeting
import ru.sexit.platform.domain.service.BbbMeetingService
import ru.sexit.platform.utils.RequestMode

@RestController
@RequestMapping("/api/dev-only/meetings")
class DevBbbMeetingController(
    private val bbbMeetingService: BbbMeetingService,
) {
    @PostMapping
    fun createMeeting(
        applicationId: Long,
    ): BbbMeeting = bbbMeetingService.createMeeting(applicationId)

    @PostMapping("/join")
    fun joinMeeting(
        applicationId: Long,
        mode: RequestMode,
    ): JoinMeetingView = JoinMeetingView(
        joinUrl = bbbMeetingService.joinMeeting(
            applicationId = applicationId,
            mode = mode,
        )
    )
}
