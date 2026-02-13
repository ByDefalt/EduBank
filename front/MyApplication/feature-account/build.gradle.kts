plugins {
    // 1. D'abord le plugin de bibliothèque Android
    alias(libs.plugins.android.library)

    // 2. Ensuite le plugin Kotlin (obligatoire avant KSP)
    alias(libs.plugins.kotlin.android)

    // 3. Enfin KSP et les autres
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spotless)
}

android {
    namespace = "defalt.featureAccount"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":core"))
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.49.0").editorConfigOverride(
            mapOf(
                "ktlint_standard_no-wildcard-imports" to "disabled", // ou "enabled" selon ton choix
                "ij_kotlin_imports_layout" to "*"
            )
        )
    }
    format("misc") {
        target("**/*.gradle", "**/*.md")
        trimTrailingWhitespace()
        endWithNewline()
    }
}