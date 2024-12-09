package ru.sexit.platform.api.http.referralprogram

import org.springframework.web.bind.annotation.*
import ru.sexit.platform.api.http.PageView
import ru.sexit.platform.api.http.applications.ApplicationView
import ru.sexit.platform.api.http.applications.NewApplicationRequest
import ru.sexit.platform.api.http.profile.PsychoProfileForCatalogueView
import ru.sexit.platform.api.http.profile.friend.model.FriendshipRequest
import ru.sexit.platform.api.http.slot.SlotMonthView
import ru.sexit.platform.api.http.toView
import ru.sexit.platform.domain.model.FriendshipProjectionByPsycho
import ru.sexit.platform.domain.model.toView
import ru.sexit.platform.domain.service.FriendService
import ru.sexit.platform.domain.service.FriendshipService
import ru.sexit.platform.domain.service.ReferralService
import ru.sexit.platform.infrastructure.security.getRequestAuthorUserInfo


/**
uc:
2. получить доступных психологов (друган)
3. создать запрос на дружбу (друган)
4. получить запрос на дружбу (псих)
5. согласиться на дружбу (псих)
6. удалить дружбу (друган/псих) (потом)
7. получить статус дружбы (друган)
8. генерировать ссылку приглашение?? (друган)
9. создать запрос на консультацию по реферальной программе
 **/

@RequestMapping("/api/v1/referral")
@RestController
class ReferralProgramController(
    private val referralService: ReferralService,
    private val profileService: FriendService,
    private val friendshipService: FriendshipService
) {

    @GetMapping("/available-psycho")
    fun getAllAvailablePsycho(
        @RequestParam(required = false) pageNumber: Int? = 0,
        @RequestParam(required = false) pageSize: Int? = 6,
    ): PageView<PsychoProfileForCatalogueView> = profileService.getAvailablePsycho(
        pageNumber = pageNumber ?: 0,
        pageSize = pageSize ?: 6,
    ).toView { it.toView() }

    @PostMapping("/create-friend")
    fun createFriendship(
        @RequestBody request: FriendshipRequest,
    ) = profileService.createFriendship(
        psychoId = request.psychoId.toLong()
    )

    @GetMapping("/my-psycho")
    fun getFriendFriendship(): FriendRefersView? {
        return profileService.getFriendshipProjection()
    }

    @GetMapping("/my-friend")
    fun getFriendFriendshipForPsycho(
    ): FriendshipProjectionByPsycho {
        return friendshipService.getFriendshipProjectionForPsycho()
    }

    @PostMapping("/my-friend")
    fun updateFriendship(
        @RequestBody request: UpdateReferRequest
    ) {
        return friendshipService.updateFriendship(request)
    }

    @PostMapping("/create-refer")
    fun createReferralProgram(): String {
        return profileService.createReferralProgram()
    }

//    @GetMapping("/{referId}") // вот сюда вот ссылка при рефералочке
//    fun getReferralProgram(
//        @RequestParam(required = true) referId: Int,
//        @RequestParam(required = false) yearId: Int?,
//        monthId: Int,
//    ): List<SlotMonthView> = referralService.getAllSlotsOfReferralProgramByPsychoId(
//        referId = referId.toLong(),
//        yearId = yearId ?: 2024,
//        monthId = monthId
//    )

//    @PostMapping("/{referId}")
//    fun createReferralApplication(
//        @RequestParam(required = true) referId: Int,
//        @RequestBody applicationRequest: NewApplicationRequest
//    ): ApplicationView {
//        return referralService.createApplicationByReferralProgram(referId.toLong(), applicationRequest).toView()
//    }

}