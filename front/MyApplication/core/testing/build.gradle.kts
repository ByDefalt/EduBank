plugins {
    kotlin("jvm")
    jacoco
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    implementation(project(":core:utils"))
    implementation(project(":core:domain"))

    api(libs.junit)
    api(libs.mockk)
    api(libs.kotlinx.coroutines.test)
    api(libs.kotlintest.runner)
}