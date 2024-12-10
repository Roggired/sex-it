package ru.sexit.platform.api.http.profile.friend

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.sexit.platform.api.http.profile.friend.model.FriendProfileView
import ru.sexit.platform.api.http.profile.friend.model.FriendRequest
import ru.sexit.platform.api.http.profile.friend.model.toView
import ru.sexit.platform.domain.service.FriendService


@RestController
@RequestMapping("/api/v1/friend/profile")
class FriendProfileController(
    private val profileService: FriendService

) {

    @PatchMapping("/{id}")
    fun createFriend(
        @PathVariable id: Long,
        @RequestBody @Validated request: FriendRequest,
    ): FriendProfileView = profileService.createOrUpdateFriend(
        id = id,
        request = request,
    )

    @GetMapping("/my")
    fun getMyProfile(): FriendProfileView = profileService.getMyProfile().toView()

}