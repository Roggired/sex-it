package ru.sexit.platform.infrastructure.security

enum class KeycloakRoles(
    val permissions: List<SexItPermissions>,
) {
    PSYCHO(
        listOf(
            SexItPermissions.PSYCHO,
        )
    ),
    PSYCHO_FRIEND(
        listOf(
            SexItPermissions.PSYCHO_FRIEND,
        )
    ),
    CLIENT(
        listOf(
            SexItPermissions.CLIENT,
        )
    ),
    ADMIN(
        listOf(
            SexItPermissions.ADMIN,
        )
    ),
    ;
}

enum class SexItPermissions {
    PSYCHO,
    CLIENT,
    PSYCHO_FRIEND,
    ADMIN,
    ;
}
