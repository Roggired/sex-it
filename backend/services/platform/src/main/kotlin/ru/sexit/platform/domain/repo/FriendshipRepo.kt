package ru.sexit.platform.domain.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.Friendship
import ru.sexit.platform.domain.model.FriendshipProjectionByFriend
import ru.sexit.platform.domain.model.FriendshipProjectionByPsycho

@Repository
interface FriendshipRepo: JpaRepository<Friendship, Long> {

    @Query(
        """
            SELECT p.name as name, f.status as status
                FROM friendship f
                LEFT JOIN psycho_profiles p on p.id = f.psycho_id
            WHERE f.friend_id = :friendId
           
        """, nativeQuery = true
    )
    fun getFriendshipProjectionForFriend(friendId: Long, pageable: Pageable): Page<FriendshipProjectionByFriend>

    @Query(
        """
            SELECT fr.name, fr.percent 
                FROM friendship 
                LEFT JOIN friend fr on fr.id = friendship.friend_id 
            WHERE fr.psycho_id = :psychoId
        """, nativeQuery = true
    )
    fun getFriendshipProjectionForPsycho(psychoId: Long): FriendshipProjectionByPsycho


    @Query(
        """
            SELECT COUNT(*) 
                FROM friendship fr
            WHERE fr.friend_id = :friendId AND fr.psycho_id = :psychoId
        """, nativeQuery = true
    )
    fun findFriendshipByFriendIdAndPsychoId(friendId: Long, psychoId: Long): Int


    fun findAllByFriendId(friendId: Long): List<Friendship>?

}