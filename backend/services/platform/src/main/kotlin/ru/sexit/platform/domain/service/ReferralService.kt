package ru.sexit.platform.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.referralprogram.ReferralProgramView
import ru.sexit.platform.domain.model.*
import ru.sexit.platform.domain.repo.ReferralProgramRepo
import ru.sexit.platform.infrastructure.exception.NotFoundException

@Service
class ReferralService(
    private val referralProgramRepo: ReferralProgramRepo,
) {

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createReferralProgram(
        friendId: Long,
        psychoId: Long,
    ): Long {
        val referralProgram = referralProgramRepo.save(
            ReferralProgram(
                id = 0L,
                psychoId = psychoId,
                friendId = friendId,
                applicationId = 0L,
                status = ReferralProgramStatus.CREATED.toString(),
                paidStatus = PaidStatus.NOT_PAID.toString(),
            )
        )
        return referralProgram.id
    }

    fun getReferralProgramById(referId: Long): ReferralProgram {
        return referralProgramRepo.findById(referId).orElseThrow { NotFoundException("referId not found") }
    }

    fun getReferralProgramByFriendId(friendId: Long, pageNumber: Int, pageSize: Int): Page<ReferralProgramView> {
        return referralProgramRepo.findReferralProgramByFriendId(
            friendId,
            pageable = PageRequest.of(pageNumber, pageSize)
        )
            .map { it.toView() }
    }

    fun getAcceptedReferralProgramByFriendId(
        friendId: Long,
        pageNumber: Int,
        pageSize: Int,
        status: String
    ): Page<ReferralProgramView> {
        return referralProgramRepo.findReferralProgramByFriendIdAndStatus(
            friendId = friendId, pageable = PageRequest.of(pageNumber, pageSize),
            status = status
        ).map { it.toView() }
    }

    fun getPaidReferralProgramByFriendId(
        friendId: Long,
        pageNumber: Int,
        pageSize: Int,
    ): Page<ReferralProgramView> {
        return referralProgramRepo.findReferralProgramByFriendIdAndStatusPaid(
            friendId = friendId, pageable = PageRequest.of(pageNumber, pageSize),
        ).map { it.toView() }
    }

    @Modifying
    @Transactional
    fun cancelReferralProgram(referId: Long) {
        referralProgramRepo.deleteById(referId)
    }

    @Transactional
    fun updateReferralPaidStatus(referId: Long) {
        referralProgramRepo.updateReferralProgramPaidStatus(referId)
    }

    fun getReferralProjectionByApplicationId(applicationId: Long): ReferralProgramForApplicationProjection{
        return referralProgramRepo.getReferralProgramApplicationProjection(applicationId)
    }
}