package ru.sexit.platform.api.http.feedback

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.sexit.platform.domain.service.FeedbackService

@RestController
@RequestMapping("/api/v1")
class FeedbackController(
    private val feedbackService: FeedbackService,
) {
    @PostMapping("/applications/{appId}/give-feedback")
    fun giveFeedback(
        @PathVariable("appId") appId: Long,
        @RequestBody @Validated request: FeedbackRequest,
    ): FeedbackView = feedbackService.createFeedback(
        appId = appId,
        request = request,
    ).toView()

    @GetMapping("/feedbacks/last-ten-by-psycho")
    fun getLastTenFeedbacksByPsycho(
        @RequestParam(required = true) psychoId: Long,
    ): List<FeedbackView> = feedbackService.getLastTenFeedbackForPsycho(
        psychoId = psychoId
    ).map { it.toView() }
}
