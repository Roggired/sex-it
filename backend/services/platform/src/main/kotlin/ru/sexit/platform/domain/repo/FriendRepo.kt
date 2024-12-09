package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.FriendProfile

@Repository
interface FriendRepo: JpaRepository<FriendProfile, Long> {
    fun findByUserId(userId: String): FriendProfile?

}