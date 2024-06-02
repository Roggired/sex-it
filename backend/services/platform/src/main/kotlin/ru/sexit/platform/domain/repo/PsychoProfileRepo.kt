package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.PsychoProfile

@Repository
interface PsychoProfileRepo : JpaRepository<PsychoProfile, Long> {
    fun findByEmail(email: String): PsychoProfile?
}
