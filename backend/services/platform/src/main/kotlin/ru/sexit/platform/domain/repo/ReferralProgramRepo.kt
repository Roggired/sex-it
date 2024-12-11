package ru.sexit.platform.domain.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import ru.sexit.platform.domain.model.ReferralProgram
import ru.sexit.platform.domain.model.ReferralProgramForApplicationProjection
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
            SELECT r.id as id, p.name as name 
            from referral_program r 
            LEFT JOIN psycho_profiles p on r.psycho_id = p.id 
            WHERE r.friend_id = :friendId and (r.status = 'CREATED' or r.status = 'CREATED_APPLICATION')
        """, nativeQuery = true
    )
    fun findReferralProgramByFriendId(
        friendId: Long,
        pageable: Pageable
    ): Page<ReferralProgramProjection>

    @Query(
        """
            SELECT r.id as id, p.name as name
                FROM referral_program r 
                LEFT JOIN psycho_profiles p on r.psycho_id = p.id 
            WHERE r.friend_id = :friendId AND r.status = :status 
        """, nativeQuery = true
    )
    fun findReferralProgramByFriendIdAndStatus(
        friendId: Long,
        status: String,
        pageable: Pageable
    ): Page<ReferralProgramProjection>

    @Query(
        """
            SELECT r.id as id, p.name as name
                FROM referral_program r 
                LEFT JOIN psycho_profiles p on r.psycho_id = p.id 
            WHERE r.friend_id = :friendId AND r.paid_status = 'PAID' 
        """, nativeQuery = true
    )
    fun findReferralProgramByFriendIdAndStatusPaid(
        friendId: Long,
        pageable: Pageable
    ): Page<ReferralProgramProjection>


    @Modifying
    @Query(
        """
          UPDATE referral_program SET paid_status = 'PAID' WHERE id = :id
        """, nativeQuery = true
    )
    fun updateReferralProgramPaidStatus(id: Long)


    @Query(
        """
            select 
                r.id as referId, f.id as friendId, f.name as name 
                from referral_program r 
                left join friend_profiles f on r.friend_id = f.id
             where r.application_id = :applicationId;
        """, nativeQuery = true
    )
    fun getReferralProgramApplicationProjection(applicationId: Long): ReferralProgramForApplicationProjection
}

