package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.Feedback

@Repository
interface FeedbackRepo : JpaRepository<Feedback, Long> {
    fun findAllByPsychoProfileId(psychoProfileId: Long): List<Feedback>
}
