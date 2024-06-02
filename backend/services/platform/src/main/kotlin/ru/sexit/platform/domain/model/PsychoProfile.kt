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
)
