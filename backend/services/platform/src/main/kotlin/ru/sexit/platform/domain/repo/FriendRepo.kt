package ru.sexit.platform.domain.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.FriendProfile
import ru.sexit.platform.domain.model.FriendshipProjectionByPsycho

@Repository
interface FriendRepo : JpaRepository<FriendProfile, Long> {
    fun findByUserId(userId: String): FriendProfile?

    fun findByEmail(email: String): FriendProfile?

    @Query(
        """
            select f.id as id, f.name as name, f.percent as percent, fr.status as status from friend f right join friendship fr on f.id = fr.friend_id where fr.psycho_id = :psychoId
        """, nativeQuery = true
    )
    fun getFriendsByPsychoId(
        psychoId: Long,
        pageable: Pageable
    ): Page<FriendshipProjectionByPsycho>

}