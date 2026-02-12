import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spotless)
}

android {
    namespace = "defalt.eduBank"
    compileSdk = 36

    defaultConfig {
        applicationId = "defalt.eduBank"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        jvmToolchain(11)
    }

    buildFeatures {
        compose = true
    }

}

dependencies {

    implementation(project(":core"))
    implementation(project(":feature-account"))
    implementation(project(":feature-offer"))
    implementation(project(":feature-bank"))
    implementation(project(":feature-operation"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.49.0").editorConfigOverride(mapOf(
            "ktlint_standard_no-wildcard-imports" to "disabled", // ou "enabled" selon ton choix
            "ij_kotlin_imports_layout" to "*"
        ))
    }
    format("misc") {
        target("**/*.gradle", "**/*.md")
        trimTrailingWhitespace()
        endWithNewline()
    }
}