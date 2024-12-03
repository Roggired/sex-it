package ru.sexit.platform.domain.model

import jakarta.persistence.*
import ru.sexit.platform.api.http.profile.PsychoProfileForCatalogueView

@Entity
@Table(name = "psycho_profiles")
class PsychoProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var userId: String,
    var name: String,
    var email: String,
    var price: Int,
    var isFirstFree: Boolean,
    var bio: String,
) {
    companion object {
        fun stub(id: Long): PsychoProfile = PsychoProfile(
            id = id,
            userId = "",
            name = "",
            email = "",
            price = 0,
            isFirstFree = false,
            bio = "",
        )
    }
}

interface PsychoProfileForCatalogueProjection {
    val id: Long
    val name: String
    val price: Int
    val rating: Double?
}

fun PsychoProfileForCatalogueProjection.toView(): PsychoProfileForCatalogueView = PsychoProfileForCatalogueView(
    id = id,
    name = name,
    price = price,
    rating = rating
)
