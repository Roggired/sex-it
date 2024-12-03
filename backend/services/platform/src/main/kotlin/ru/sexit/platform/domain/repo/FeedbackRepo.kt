package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.Feedback

@Repository
interface FeedbackRepo : JpaRepository<Feedback, Long> {
    fun findAllByPsychoProfileId(psychoProfileId: Long): List<Feedback>

    @Query(
        """
            SELECT f
            FROM Feedback f
            WHERE f.psychoProfile.id = :psychoProfileId
            ORDER BY f.creationTime DESC
            LIMIT 10
        """
    )
    fun findLastTenFeedbacksByPsychoProfileId(psychoProfileId: Long): List<Feedback>
}
