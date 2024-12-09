package ru.sexit.platform.domain.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.PsychoProfile
import ru.sexit.platform.domain.model.PsychoProfileForCatalogueProjection

@Repository
interface PsychoProfileRepo : JpaRepository<PsychoProfile, Long> {
    fun findByEmail(email: String): PsychoProfile?

    fun findByUserId(userId: String): PsychoProfile?

    @Query(
        """
            SELECT
                t.id as id,
                t.name as name,
                t.price as price,
                t.rating as rating
            FROM (
                SELECT 
                    p.id as id,
                    p.name as name,
                    p.price as price,
                    AVG(f.rating) as rating
                FROM psycho_profiles p
                LEFT JOIN feedbacks f ON p.id = f.psycho_id
                WHERE (COALESCE(:name, NULL) IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:name AS text), '%')))
                    AND (COALESCE(:priceFrom, NULL) IS NULL OR p.price >= :priceFrom)
                    AND (COALESCE(:priceTo, NULL) IS NULL OR p.price <= :priceTo)
                GROUP BY p.id, p.name, p.price
            ) AS t
            WHERE COALESCE(:minRating, NULL) IS NULL OR :minRating <= t.rating
            ORDER BY t.rating DESC NULLS LAST
        """, nativeQuery = true
    )
    fun findPagedByFilters(
        name: String?,
        priceFrom: Int?,
        priceTo: Int?,
        minRating: Double?,
        pageable: Pageable
    ): Page<PsychoProfileForCatalogueProjection>

    @Query(
        """
            SELECT
                t.id as id,
                t.name as name,
                t.price as price,
                t.rating as rating
            FROM (
                SELECT 
                    p.id as id,
                    p.name as name,
                    p.price as price,
                    AVG(f.rating) as rating
                FROM psycho_profiles p
                LEFT JOIN feedbacks f ON p.id = f.psycho_id
                WHERE p.id NOT IN (
                    SELECT psycho_id 
                    FROM friendship fr 
                )
                GROUP BY p.id, p.name, p.price
            ) AS t
            ORDER BY t.rating DESC NULLS LAST
        """, nativeQuery = true
    )
    fun findPagedAvailablePsycho(
        pageable: Pageable
    ): Page<PsychoProfileForCatalogueProjection>


    @Modifying
    @Query(
        """
        UPDATE friendships f
        SET f.status = :status
        WHERE f.psycho_id = :psychoId
    """, nativeQuery = true)
    fun updateFriendshipStatus(
        psychoId: Long,
        status: String
    )
}
