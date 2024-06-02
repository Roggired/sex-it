package ru.sexit.platform.utils

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

const val RID_MDC_KEY = "requestId"
const val UID_MDC_KEY = "userId"

@Component
@Order(1)
class LoggingFilter: Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        log.trace("Entering LoggingFilter...")
        val httpRequest = request!! as HttpServletRequest
        val filterChain = chain!!

        val securityContext = SecurityContextHolder.getContext()
        if (securityContext == null) {
            MDC.put(UID_MDC_KEY, null)
            log.trace("SecurityContext is null -> MDC (key: $UID_MDC_KEY) set to null")
        } else {
            val authentication = securityContext.authentication
            if (authentication != null && authentication.isAuthenticated) {
//                when (authentication) {
//                    is JwtAuthenticationToken -> {
//                        MDC.put(UID_MDC_KEY, authentication.name)
//                        log.trace("$UID_HEADER not presented and JwtAuthenticationToken authentication found -> MDC (key: $UID_MDC_KEY) set to ${authentication.name}")
//                    }
//                    is UserInfoAuthentication -> {
//                        MDC.put(UID_MDC_KEY, authentication.userId)
//                        log.trace("$UID_HEADER not presented and UserInfoAuthentication authentication found -> MDC (key: $UID_MDC_KEY) set to ${authentication.userId}")
//                    }
//                    else -> {
//                        MDC.put(UID_MDC_KEY, null)
//                        log.trace("$UID_HEADER not presented and unknown authentication found -> MDC (key: $UID_MDC_KEY) set to null")
//                    }
//                }
                MDC.put(UID_MDC_KEY, null)
                log.trace("Unknown authentication found -> MDC (key: $UID_MDC_KEY) set to null")
            } else {
                MDC.put(UID_MDC_KEY, null)
                log.trace("SecurityContext != null and authentication is null or is not authenticated -> MDC (key: $UID_MDC_KEY) set to null")
            }
        }

        if (log.isTraceEnabled) {
            val params = if (httpRequest.parameterMap.isEmpty()) "" else {
                "?${
                    httpRequest.parameterMap
                        .entries
                        .flatMap { param -> param.value.toList().map { param.key to it } }
                        .joinToString(separator = "&") { entry -> "${entry.first}=${entry.second}" }
                }"
            }
            val headersLog = httpRequest
                .headerNames
                .asSequence()
                .filter { !it.contains("Authentication", true) && !it.contains("Cookie", true) }
                .map { it to httpRequest.getHeader(it) }
                .sortedBy { it.first }
                .joinToString(separator = ";") { "\n\t<${it.first}=${it.second}>" }
            log.trace("Incoming request: ${httpRequest.method} ${httpRequest.requestURI}$params. Headers: " +
                    "[$headersLog]")
        }

        try {
            if (log.isTraceEnabled) {
                val preparedRequestURI = replacePathParamsInURI(
                    replaceContextPathInURI(
                        httpRequest = httpRequest,
                        requestURI = httpRequest.requestURI.toString()
                    )
                )

                EndpointElapsedTimeMeasureLogger.endpointElapsedTimeMeasureLoggingTemplate(
                    endpointName = "${httpRequest.method} $preparedRequestURI"
                ) {
                    log.trace("Leaving LoggingFilter with EndpointElapsedTimeMeasureLogger option.")
                    filterChain.doFilter(request, response)
                }
            } else {
                log.trace("Leaving LoggingFilter.")
                filterChain.doFilter(request, response)
            }
        } finally {
            MDC.remove(RID_MDC_KEY)
            MDC.remove(UID_MDC_KEY)
            log.trace("LoggingFilter post-processing - removing $RID_MDC_KEY and $UID_MDC_KEY from MDC")
        }
    }

    private fun replaceContextPathInURI(
        httpRequest: HttpServletRequest,
        requestURI: String
    ): String {
        return requestURI.replace(httpRequest.contextPath, "")
    }

    private fun replacePathParamsInURI(
        requestURI: String
    ): String {
        val inputString = if (requestURI.endsWith("/")) requestURI else "$requestURI/"
        return inputString.replace(regex = Regex("/\\d+/"), replacement = "/{param}/")
    }
}

