package ru.sexit.platform.infrastructure.integration.keycloak.admin.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakAdminAccessRepresentation

interface KeycloakAdminOAuthAPI {
    @POST("/realms/master/protocol/openid-connect/token")
    @FormUrlEncoded
    fun authenticateAdmin(
        @Field("client_id") clientId: String,
        @Field("username") adminUsername: String,
        @Field("password") adminPassword: String,
        @Field("grant_type") grantType: String = "password"
    ): Call<KeycloakAdminAccessRepresentation>

    @POST("/realms/master/protocol/openid-connect/token")
    @FormUrlEncoded
    fun refreshAdminAccess(
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String = "refresh_token"
    ): Call<KeycloakAdminAccessRepresentation>
}
