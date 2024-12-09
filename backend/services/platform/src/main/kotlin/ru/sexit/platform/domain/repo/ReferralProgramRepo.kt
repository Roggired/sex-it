package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import ru.sexit.platform.domain.model.ReferralProgram

interface ReferralProgramRepo: JpaRepository<ReferralProgram, Long> {

    @Modifying
    @Query(
        """
          UPDATE referral_program SET status = :status WHERE id = :id;  
        """, nativeQuery = true
    )
    fun updateReferralProgramStatus(id: Long, status: String)
}