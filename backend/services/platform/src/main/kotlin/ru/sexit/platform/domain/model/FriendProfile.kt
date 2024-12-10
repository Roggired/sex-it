package ru.sexit.platform.domain.model

import jakarta.persistence.*
import ru.sexit.platform.api.http.referralprogram.FriendRefersView
import ru.sexit.platform.api.http.referralprogram.PsychoRefersView

@Entity
@Table(name = "friend_profiles")
class FriendProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var userId: String,
    var name: String,
    var email: String,
    var percent: Int,
)


enum class FriendshipStatus {
    CREATED,
    ACCEPTED,
    REJECTED
    ;
}

@Entity
@Table(name = "friendship")
class Friendship(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val friendId: Long,
    val psychoId: Long,
    @Enumerated(EnumType.STRING)
    val status: FriendshipStatus
)

interface FriendshipProjectionByFriend {
    val name: String
    val status: String
}

fun FriendshipProjectionByFriend.toView(): FriendRefersView = FriendRefersView(
    psychoName = name,
    friendshipStatus = status
)

interface FriendshipProjectionByPsycho {
    val id: Long
    val name: String
    val friendPercent: Int
    val status: String
}

fun FriendshipProjectionByPsycho.toView(): PsychoRefersView = PsychoRefersView(
    id = id,
    friendName = name,
    percent = friendPercent,
    status = status
)
