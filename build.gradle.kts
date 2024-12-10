import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "br.com.nubank"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    testImplementation("io.mockk:mockk:1.13.2")
    
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0")
    testImplementation("org.junit.platform:junit-platform-launcher:1.7.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.30")

    implementation("com.google.code.gson:gson:2.11.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks.shadowJar {
    archiveBaseName.set("capital-gain-service")
    archiveVersion.set("1.0")
}