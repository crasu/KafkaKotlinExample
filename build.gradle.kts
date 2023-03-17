plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "de.bafza.kafka.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.kafka:kafka_2.13:3.4.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(16)
}

application {
    mainClass.set("MainKt")
}
