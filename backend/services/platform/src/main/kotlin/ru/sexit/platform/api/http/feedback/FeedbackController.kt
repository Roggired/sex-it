package ru.sexit.platform.api.http.feedback

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.sexit.platform.domain.service.FeedbackService

@RestController
@RequestMapping("/api/v1/applications/{appId}")
class FeedbackController(
    private val feedbackService: FeedbackService,
) {
    @PostMapping("/give-feedback")
    fun giveFeedback(
        @PathVariable("appId") appId: Long,
        @RequestBody @Validated request: FeedbackRequest,
    ): FeedbackView = feedbackService.createFeedback(
        appId = appId,
        request = request,
    ).toView()
}
