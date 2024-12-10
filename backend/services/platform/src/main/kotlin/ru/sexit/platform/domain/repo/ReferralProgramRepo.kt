package ru.sexit.platform.domain.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import ru.sexit.platform.domain.model.ReferralProgram
import ru.sexit.platform.domain.model.ReferralProgramProjection

interface ReferralProgramRepo : JpaRepository<ReferralProgram, Long> {

    @Modifying
    @Query(
        """
          UPDATE referral_program SET status = :status WHERE id = :id;  
        """, nativeQuery = true
    )
    fun updateReferralProgramStatus(id: Long, status: String)


    @Query(
        """
            SELECT p.name as name, r.paid_status as paidStatus from referral_program r LEFT JOIN psycho_profiles p on r.psycho_id = p.id WHERE r.friend_id = :friendId
        """, nativeQuery = true
    )
    fun findReferralProgramByFriendId(
        friendId: Long,
        pageable: Pageable
    ): Page<ReferralProgramProjection>

    @Modifying
    @Query(
        """
          UPDATE referral_program SET paid_status = 'PAID' WHERE id = :id;  
        """, nativeQuery = true
    )
    fun updateReferralProgramPaidStatus(id: Long)
}

