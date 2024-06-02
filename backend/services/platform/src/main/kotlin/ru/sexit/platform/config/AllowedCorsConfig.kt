package ru.sexit.platform.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AllowedCorsConfig(
    @Value("\${cors.allowed-origins}")
    private val allowedOrigins: Array<String>
): WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*allowedOrigins)
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
