package ru.sexit.platform.domain.service

import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.domain.model.ReferralProgram
import ru.sexit.platform.domain.model.ReferralProgramStatus
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
            )
        )
        return referralProgram.id
    }

    fun getReferralProgramById(referId: Long): ReferralProgram {
        return referralProgramRepo.findById(referId).orElseThrow { NotFoundException("referId not found") }
    }

    @Modifying
    fun cancelReferralProgram(referId: Long) {
        referralProgramRepo.deleteById(referId)
    }
}