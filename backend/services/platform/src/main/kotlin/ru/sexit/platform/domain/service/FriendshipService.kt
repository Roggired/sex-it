package ru.sexit.platform.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.profile.PsychoProfileForFriendshipView
import ru.sexit.platform.api.http.profile.friend.FriendProfileController
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
    @Autowired
    @Lazy
    lateinit var psychoProfileService: PsychoProfileService

    @Autowired
    @Lazy
    lateinit var friendService: FriendService

    // createFriendship -- создается дружба между друганом и психологом, инициатор: друган
    fun createFriendship(
        psychoId: Long,
        friendId: Long
    ) {
        val count = friendshipRepo.findFriendshipByFriendIdAndPsychoId(friendId, psychoId)
        if (count > 0) {
            throw AlreadyExistException("friendship of friend: $friendId and psycho: $psychoId already exists")
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
    @Transactional
    fun updateFriendship(
        request: UpdateReferRequest
    ) {
        psychoRepo.updateFriendshipStatus(
            psychoId = psychoProfileService.getMyProfile().id,
            friendId = request.friendId,
            status = request.status,
        )
    }


    // getAllAvailablePsycho -- получаем всех доступных психологов (без дружбы)
    fun getAllAvailablePsycho(
        name: String?,
        friendId: Long,
        pageNumber: Int,
        pageSize: Int
    ): Page<PsychoProfileForFriendshipView> {
        return psychoRepo.findPagedAvailablePsycho(
            name = name,
            friendId = friendId,
            pageable = PageRequest.of(pageNumber, pageSize)
        ).map { it.toView() }
    }

    // getFriendship -- получаем дружбу с психологом (смотрим статус по факту)
    fun getFriendshipProjectionForFriend(
        friendId: Long,
        pageable: Pageable,
        psychoName: String?,
    ): Page<FriendshipProjectionByFriend> {
        return friendshipRepo.getFriendshipProjectionForFriend(friendId, pageable, psychoName)
    }

    fun getFriendship(
        friendId: Long
    ): List<Friendship> {
        return friendshipRepo.findAllByFriendId(friendId)
            ?: throw NotFoundException("there is no friendship by this id: $friendId")
    }

    // getFriendship -- получаем дружбу с друганом (для психа)
    fun getFriendshipProjectionForPsycho(
    ): FriendshipProjectionByPsycho {
        val psycho =
            psychoRepo.findByUserId(getRequestAuthorUserInfo().id) ?: throw NotFoundException("psycho not found")
        return friendshipRepo.getFriendshipProjectionForPsycho(psycho.id)
    }

    @Transactional
    fun deleteFriendship(psychoId: Long) {
        friendshipRepo.deleteByFriendIdAndPsychoId(
            friendId = friendService.getMyProfile().id,
            psychoId = psychoId,
        )
    }
}