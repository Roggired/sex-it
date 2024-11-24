package ru.sexit.platform.infrastructure.integration.keycloak.admin.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakUserRepresentation

interface KeycloakAdminAPI {
    @GET("users/{id}")
    fun getUserRepresentationById(
        @Path("id") id: String,
    ): Call<KeycloakUserRepresentation>

    @PUT("users/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body user: KeycloakUserRepresentation,
    ): Call<Unit>

    @GET("roles/{roleName}/users")
    fun getUsersWithRole(
        @Path("roleName") roleName: String,
        @Query("briefRepresentation") briefRepresentation: Boolean = true,
        @Query("first") first: Int?,
        @Query("max") max: Int,
    ): Call<List<KeycloakUserRepresentation>>
}
