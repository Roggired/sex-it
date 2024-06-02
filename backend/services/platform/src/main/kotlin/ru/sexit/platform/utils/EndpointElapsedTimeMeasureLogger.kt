package ru.sexit.platform.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.MDC

class EndpointElapsedTimeMeasureLogger {
    companion object {
        @Suppress("UNUSED")
        fun <T> endpointElapsedTimeMeasureLoggingTemplate(
            endpointName: String,
            func: () -> T
        ): T {
            val startMillis = System.currentTimeMillis()
            val objectMapper = ObjectMapper()

            try {
                val result = func.invoke()
                val endMillis = System.currentTimeMillis()

                log.trace(
                    objectMapper.writeValueAsString(
                        EndpointElapsedTimeLog(
                            endpointName = endpointName,
                            elapsedTimeMillis = endMillis - startMillis
                        )
                    )
                )
                return result
            } catch (e: Exception) {
                val endMillis = System.currentTimeMillis()
                log.trace(
                    objectMapper.writeValueAsString(
                        EndpointElapsedTimeLog(
                            endpointName = endpointName,
                            isError = true,
                            errorName = e::class.java.name,
                            elapsedTimeMillis = endMillis - startMillis
                        )
                    )
                )
                throw e
            }
        }
    }
}

data class EndpointElapsedTimeLog(
    val RID: String? = MDC.get(RID_MDC_KEY),
    val UID: String? = MDC.get(UID_MDC_KEY),
    val searchConstantKey: String = "ENDPOINT_ELAPSED_TIME_LOG",
    val endpointName: String,
    val isError: Boolean = false,
    val errorName: String? = null,
    val elapsedTimeMillis: Long
)
