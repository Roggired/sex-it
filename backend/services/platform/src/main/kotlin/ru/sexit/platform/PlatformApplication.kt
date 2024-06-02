package ru.sexit.platform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication(exclude = [
    UserDetailsServiceAutoConfiguration::class
])
@ConfigurationPropertiesScan("ru.sexit.platform.config.properties")
class PlatformApplication

fun main(args: Array<String>) {
    runApplication<PlatformApplication>(*args)
}
