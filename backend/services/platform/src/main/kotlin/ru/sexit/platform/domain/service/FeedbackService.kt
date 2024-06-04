package ru.sexit.platform.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.feedback.FeedbackRequest
import ru.sexit.platform.domain.model.ApplicationEntity
import ru.sexit.platform.domain.model.Feedback
import ru.sexit.platform.domain.model.PsychoProfile
import ru.sexit.platform.domain.repo.FeedbackRepo
import ru.sexit.platform.utils.currentUTCTime
import ru.sexit.platform.utils.log

@Service
class FeedbackService(
    private val feedbackRepo: FeedbackRepo,
) {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createFeedback(appId: Long, request: FeedbackRequest): Feedback = feedbackRepo.save(
        Feedback(
            id = 0L,
            creationTime = currentUTCTime(),
            rating = request.rating,
            text = request.text,
        ).apply {
            psychoProfile = PsychoProfile.stub(1L)
            applicationEntity = ApplicationEntity.stub(appId)
        }
    ).also {
        log.info("New feedback (id: ${it.id}) has been created")
    }
}
