package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.BbbMeeting

@Repository
interface BbbMeetingRepo: JpaRepository<BbbMeeting, Long> {
    fun findByApplicationId(applicationId: Long): BbbMeeting?
}
