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

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
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