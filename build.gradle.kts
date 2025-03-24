plugins {
    val kotlinVersion = "1.9.25"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion

    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion

    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.task"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

val flywayVersion by extra { "10.4.1" }
val loggerVersion by extra { "5.1.0" }
val redissonVersion by extra { "3.21.3" }
dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation ("org.springframework.boot:spring-boot-starter-web")

    /** database */
    implementation("redis.clients:jedis:4.3.1")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.redisson:redisson-spring-boot-starter:$redissonVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java:8.0.33")
    runtimeOnly("com.h2database:h2")

    /** flyway */
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.flywaydb:flyway-mysql:$flywayVersion")

    val queryDslVersion = dependencyManagement.importedProperties["querydsl.version"]
    implementation("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
    kapt("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")

    kapt("jakarta.persistence:jakarta.persistence-api")
    kapt("jakarta.annotation:jakarta.annotation-api")

    // logging
    implementation("io.github.oshai:kotlin-logging-jvm:$loggerVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
