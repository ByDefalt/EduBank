plugins {
    kotlin("jvm")
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
    /* ---------------- COROUTINES ---------------- */
    api(libs.kotlinx.coroutines.core)

    /* ---------------- DEPENDENCY INJECTION (KOIN) ---------------- */
    api(libs.koin.core)

    /* ---------------- TESTS ---------------- */
    testApi(libs.junit)
    testApi(libs.kotlintest.runner)
    testApi(libs.kotlinx.coroutines.test)


    api(libs.kotlinx.serialization.json)
}