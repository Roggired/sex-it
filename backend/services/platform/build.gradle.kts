import org.springframework.boot.gradle.tasks.bundling.BootJar

val springKafkaVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
    kotlin("kapt")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

allOpen {
    annotations(
        "jakarta.persistence.MappedSuperclass",
        "jakarta.persistence.Entity",
        "org.springframework.data.redis.core.RedisHash",
        "org.springframework.data.redis.core.TimeToLive"
    )
}

dependencies {
    kapt("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    // spring starters
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.keycloak:keycloak-core:26.0.6")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server:6.1.3")
    implementation("org.springframework.security:spring-security-oauth2-jose:6.1.3")

    // jackson
    implementation("org.jetbrains.kotlin:kotlin-reflect") // spring data introspection
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.2")

    // validation
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")

    // documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Http client
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // database
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")

    // other
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-codec:commons-codec:1.17.0")

    // test
    testImplementation("io.kotest:kotest-runner-junit5:5.5.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.13.2")

    testImplementation("org.testcontainers:testcontainers:1.17.4")
    testImplementation("org.testcontainers:postgresql:1.17.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("io.github.bonigarcia:webdrivermanager:5.9.2")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.27.0")
    //testImplementation("org.seleniumhq.selenium:selenium-chrome-driver:4.27.0")
}

springBoot {
    buildInfo()
}

tasks.withType<BootJar> {
    archiveFileName.set("platform.war")
}

tasks.withType<Test> {
    useJUnitPlatform()
    this.environment["SPRING_PROFILES_ACTIVE"] = "test"
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

kapt {
    useBuildCache = true
}
