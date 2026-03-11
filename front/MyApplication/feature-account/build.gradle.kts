plugins {
    // 1. D'abord le plugin de bibliothèque Android
    alias(libs.plugins.android.library)

    // 2. Ensuite le plugin Kotlin (obligatoire avant KSP)
    alias(libs.plugins.kotlin.android)

    // 3. Enfin KSP et les autres
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "defalt.featureAccount"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
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
    implementation(project(":core:domain"))
    implementation(project(":core:utils"))
    implementation(project(":core:ui"))
    testImplementation(project(":core:testing"))
}