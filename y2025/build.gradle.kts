plugins {
    kotlin("jvm") version "2.2.20"
}

group = "dev.mickyloo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // z3
    implementation("tools.aqua:z3-turnkey:4.14.1")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}