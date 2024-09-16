import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    api(project(":lib"))
    testImplementation(project(":testlib"))

    testImplementation(libs.kotlinx.serialization.json)

    testImplementation(libs.jupiter.api)
    testImplementation(libs.jupiter.parameters)

    testRuntimeOnly(libs.jupiter.engine)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
