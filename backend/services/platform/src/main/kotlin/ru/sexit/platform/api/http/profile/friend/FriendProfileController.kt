package ru.sexit.platform.api.http.profile.friend

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.sexit.platform.api.http.profile.friend.model.FriendProfileView
import ru.sexit.platform.api.http.profile.friend.model.FriendRequest
import ru.sexit.platform.domain.service.FriendService


@RestController
@RequestMapping("/api/v1/friend/profile")
class FriendProfileController(
    private val profileService: FriendService

) {

    @PostMapping
    fun createFriend(
        @RequestBody @Validated request: FriendRequest,
    ): FriendProfileView = profileService.createFriend(
        request = request,
    )

}