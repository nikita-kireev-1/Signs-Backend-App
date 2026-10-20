plugins {
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.serialization") version "2.4.20"
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"
val ktorVersion = "2.3.9"
val coroutinesVersion = "1.7.1"

repositories {
    mavenCentral()
}

configurations.configureEach {
    resolutionStrategy.force(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.1",
        "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.1",
        "org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.7.1"
    )
}

dependencies {
    //testImplementation(kotlin("test-junit5"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.4.20")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.4")
    implementation("io.github.smiley4:ktor-swagger-ui:2.8.0")
    implementation(platform("io.ktor:ktor-bom:$ktorVersion"))
    implementation("io.swagger.core.v3:swagger-annotations:2.2.20")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-status-pages")
    implementation("io.ktor:ktor-server-compression")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.exposed:exposed-core:1.5.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.5.0")
    implementation("org.jetbrains.exposed:exposed-dao:1.5.0")
    implementation("org.jetbrains.exposed:exposed-java-time:1.5.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    implementation("org.flywaydb:flyway-core:10.10.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.10.0")
    implementation("io.insert-koin:koin-ktor:3.5.3")
    implementation("io.insert-koin:koin-logger-slf4j:3.5.3")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("com.auth0:java-jwt:4.6.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    testImplementation("io.insert-koin:koin-test:3.5.3")
    testImplementation("io.insert-koin:koin-test-junit5:3.5.3")
}

application {
    mainClass.set("ApplicationKt")
}

kotlin {
    jvmToolchain(26)
}

tasks.test {
    useJUnitPlatform()
}