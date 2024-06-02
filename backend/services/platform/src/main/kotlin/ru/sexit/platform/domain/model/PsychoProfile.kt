package ru.sexit.platform.domain.model

import jakarta.persistence.*

@Entity
@Table(name = "psycho_profiles")
class PsychoProfile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var name: String,
    var email: String,
    var price: Int,
    var isFirstFree: Boolean,
    var bio: String,
) {
    companion object {
        fun stub(id: Long): PsychoProfile = PsychoProfile(
            id = id,
            name = "",
            email = "",
            price = 0,
            isFirstFree = false,
            bio = "",
        )
    }
}
