package ru.sexit.platform.domain.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.sexit.platform.api.http.referralprogram.UpdateReferRequest
import ru.sexit.platform.domain.model.*
import ru.sexit.platform.domain.repo.FriendshipRepo
import ru.sexit.platform.domain.repo.PsychoProfileRepo
import ru.sexit.platform.infrastructure.exception.AlreadyExistException
import ru.sexit.platform.infrastructure.exception.NotFoundException
import ru.sexit.platform.infrastructure.security.getRequestAuthorUserInfo

@Service
class FriendshipService(
    private val friendshipRepo: FriendshipRepo,
    private val psychoRepo: PsychoProfileRepo
) {

    // createFriendship -- создается дружба между друганом и психологом, инициатор: друган
    fun createFriendship(
        psychoId: Long,
        friendId: Long
    ) {
        val friend = friendshipRepo.findFriendshipByFriendId(friendId)
        if (friend != null) {
            throw AlreadyExistException("friendship of friend: $friendId already exists")
        }
        friendshipRepo.save(
            Friendship(
                id = 0L,
                psychoId = psychoId,
                friendId = friendId,
                status = FriendshipStatus.CREATED
            )
        )
    }

    // updateFriendship -- дружба между друганом и психологом подтверждается/отрицается, инициатор: психолог
    fun updateFriendship(
        request: UpdateReferRequest
    ) {
        psychoRepo.updateFriendshipStatus(
            psychoId = request.psychoId,
            status = request.status,
        )
    }


    // getAllAvailablePsycho -- получаем всех доступных психологов (без дружбы)
    fun getAllAvailablePsycho(
        pageNumber: Int,
        pageSize: Int
    ): Page<PsychoProfileForCatalogueProjection> {
        return psychoRepo.findPagedAvailablePsycho(
            pageable = PageRequest.of(pageNumber, pageSize)
        )
    }

    // getFriendship -- получаем дружбу с психологом (смотрим статус по факту)
    fun getFriendshipProjectionForFriend(
        friendId: Long,
    ): FriendshipProjectionByFriend? {
        return friendshipRepo.getFriendshipProjectionForFriend(friendId)
    }

    fun getFriendship(
        friendId: Long
    ): Friendship {
        return friendshipRepo.findFriendshipByFriendId(friendId)
            ?: throw NotFoundException("there is no friendship by this id: $friendId")
    }

    // getFriendship -- получаем дружбу с друганом (для психа)
    fun getFriendshipProjectionForPsycho(
    ): FriendshipProjectionByPsycho {
        val psycho =
            psychoRepo.findByUserId(getRequestAuthorUserInfo().id) ?: throw NotFoundException("psycho not found")
        return friendshipRepo.getFriendshipProjectionForPsycho(psycho.id)
    }
}