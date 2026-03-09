import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    jacoco
}

// Lecture de local.properties
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}

android {
    namespace = "defalt.network"
    compileSdk { version = release(36) }
    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "GATEWAY_URL",
            "\"${localProperties.getProperty("GATEWAY_URL", "")}\"",
        )
        buildConfigField(
            "Boolean",
            "FAKE_DATA_IS_ACTIVE",
            localProperties.getProperty("FAKE_DATA_IS_ACTIVE", ""),
        )
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:utils"))
    testImplementation(project(":core:testing"))

    /* ---------------- NETWORK ---------------- */
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.converter.scalars)

    /* ---------------- TESTS ---------------- */
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}