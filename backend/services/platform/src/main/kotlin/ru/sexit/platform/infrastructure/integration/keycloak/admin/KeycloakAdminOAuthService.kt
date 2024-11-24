package ru.sexit.platform.infrastructure.integration.keycloak.admin

import okhttp3.Request
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import ru.sexit.platform.config.properties.KeycloakAdminProperties
import ru.sexit.platform.infrastructure.exception.UnauthorizedFromDownstreamServiceException
import ru.sexit.platform.infrastructure.integration.DownstreamServices
import ru.sexit.platform.infrastructure.integration.IntegrationRetrofitClient
import ru.sexit.platform.infrastructure.integration.keycloak.admin.api.KeycloakAdminOAuthAPI
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakAdminAccessRepresentation
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakError
import ru.sexit.platform.utils.currentUTCTime
import ru.sexit.platform.utils.log
import java.time.LocalDateTime
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

const val MAX_ACCESS_EXPIRE_THRESHOLD_SEC = 2L

/**
 * KeycloakAdminOAuthService is designed to be invoked inside OkHttp's interceptor to add credentials to a
 * request before it will be executed. It stores Keycloak's access and refresh tokens and does it as effective
 * as it only possible by using shared memory between all threads and all request. Synchronization mechanism for
 * this shared memory is based on:
 * 1. ReadWriteLock - so any number of parallel read requests supported and only one write request
 * 2. Double-checking write lock - with only one check for write lock there is a possibility that several threads
 * will obtain access from Keycloak Admin REST API. We do double-check to prevent possible double obtain requests.
 *
 * Read comments on addCredentials() for more details on synchronization.
 *
 * @author ego
 */
@Component
@Scope("singleton")
class KeycloakAdminOAuthService(
    private val keycloakAdminProperties: KeycloakAdminProperties,
    private val keycloakAdminOAuthAPI: KeycloakAdminOAuthAPI,
    @Qualifier("keycloakAdminIntegrationRetrofitClient")
    private val integrationClient: IntegrationRetrofitClient<KeycloakError>,
) {
    private val readWriteLock: ReadWriteLock = ReentrantReadWriteLock()
    private var access: KeycloakAdminAccessRepresentation? = null
    private var accessGrantedDate: LocalDateTime? = null

    fun addCredentials(requestBuilder: Request.Builder) {
        // Firstly, we need to read access and define which of 3 possible actions do we need:
        // 1. Use current access token
        // 2. Use refresh token to obtain new access token
        // 3. Use credentials to obtain new access token
        readWriteLock.readLock().lockInterruptibly()
        var examineResult = examineCurrentAccess()
        var accessGranted = examineResult.first

        // in same read lock use current access
        if (accessGranted) {
            addAuthorizationHeader(requestBuilder)
            readWriteLock.readLock().unlock()
            log.trace("Access granted using existed access token")
            return
        }

        readWriteLock.readLock().unlock()

        // we need write lock to modify access
        readWriteLock.writeLock().lockInterruptibly()
        log.trace("Got exclusive WRITE lock. Reexamine access to prevent doubling of refresh or obtain requests")
        examineResult = examineCurrentAccess()
        accessGranted = examineResult.first
        val refreshAccess = examineResult.second
        var obtainAccess = examineResult.third

        if (accessGranted) {
            addAuthorizationHeader(requestBuilder)
            readWriteLock.writeLock().unlock()
            log.trace("Reexamination showed that access is not expired - access granted using existed access token")
            return
        }

        log.trace("Getting access...")
        // we don't need to check for access == null second time, because we never write null to access
        if (refreshAccess) {
            try {
                access = integrationClient.invokeExternalService(
                    downstreamService = DownstreamServices.KEYCLOAK,
                ) {
                    keycloakAdminOAuthAPI.refreshAdminAccess(
                        clientId = keycloakAdminProperties.adminClientId,
                        refreshToken = access!!.refreshToken,
                    )
                }.content!!
                accessGrantedDate = currentUTCTime()
                // obtainAccess is false here
                log.trace("Getting access DONE. Access has been refresh with refresh token")
            } catch (e: UnauthorizedFromDownstreamServiceException) {
                // fail to refresh access
                obtainAccess = true
                log.trace("Getting access FAILED. Refresh token is expired - OBTAIN")
            } catch (e: Throwable) {
                // we should re-throw other exceptions, but we also need to unlock() lock before actual throwing
                readWriteLock.writeLock().unlock()
                throw e
            }
        }

        if (obtainAccess) {
            try {
                access = integrationClient.invokeExternalService(
                    downstreamService = DownstreamServices.KEYCLOAK,
                ) {
                    keycloakAdminOAuthAPI.authenticateAdmin(
                        clientId = keycloakAdminProperties.adminClientId,
                        adminUsername = keycloakAdminProperties.adminUsername,
                        adminPassword = keycloakAdminProperties.adminPassword,
                    )
                }.content!!
                accessGrantedDate = currentUTCTime()
                log.trace("Getting access DONE. Access has been obtained using credentials")
            } catch (e: Throwable) {
                // we should re-throw other exceptions, but we also need to unlock() lock before actual throwing
                readWriteLock.writeLock().unlock()
                throw e
            }
        }

        addAuthorizationHeader(requestBuilder)
        readWriteLock.writeLock().unlock()
        log.trace("Access granted using refreshed or obtained access token")
    }

    private fun examineCurrentAccess(): Triple<Boolean, Boolean, Boolean> {
        log.trace("Examining current access...")
        var accessGranted = false
        var refreshAccess = false
        var obtainAccess = false
        if (accessGrantedDate == null || access == null) {
            // obtain access first time
            obtainAccess = true
            log.trace("Examining current access DONE. No access found - OBTAIN")
        } else if (
            accessGrantedDate!!
                .plusSeconds(access!!.expiresIn)
                .minusSeconds(MAX_ACCESS_EXPIRE_THRESHOLD_SEC) < currentUTCTime()
        ) {
            // try to refresh access
            if (
                accessGrantedDate!!
                    .plusSeconds(access!!.refreshExpiresIn)
                    .minusSeconds(MAX_ACCESS_EXPIRE_THRESHOLD_SEC) < currentUTCTime()
            ) {
                obtainAccess = true
                log.trace("Examining current access DONE. Access & Refresh tokens expired - OBTAIN")
            } else {
                refreshAccess = true
                log.trace("Examining current access DONE. Access token expired - REFRESH")
            }
        } else {
            // execute request with current access
            accessGranted = true
            log.trace("Examining current access DONE. Current access token is not expired - GRANT")
        }

        return Triple(accessGranted, refreshAccess, obtainAccess)
    }

    private fun addAuthorizationHeader(requestBuilder: Request.Builder) {
        requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer ${access!!.accessToken}")
    }
}
