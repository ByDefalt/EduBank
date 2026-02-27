plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "defalt.ui"
    compileSdk = 36
    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions { jvmTarget = "11" }
}

dependencies {
    implementation(project(":core:testing"))
    implementation(project(":core:domain"))
    implementation(project(":core:utils"))

    /* ---------------- CORE ANDROID ---------------- */
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)

    /* ---------------- COMPOSE ---------------- */
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.extended)
    debugApi(libs.androidx.compose.ui.tooling)
    debugApi(libs.androidx.compose.ui.test.manifest)

    /* ---------------- NAVIGATION ---------------- */
    api(libs.androidx.navigation.compose)

    /* ---------------- VIEWMODEL ---------------- */
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.lifecycle.runtime.compose)

    /* ---------------- KOIN Compose ---------------- */
    api(libs.koin.compose)
    api(libs.koin.android)

    /* ---------------- IMAGES ---------------- */
    api(libs.coil.compose)

    /* ---------------- SPLASHSCREEN ---------------- */
    api(libs.androidx.core.splashscreen)

    /* ---------------- PERMISSIONS ---------------- */
    api(libs.accompanist.permissions)

    /* ---------------- COROUTINE ---------------- */
    api(libs.kotlinx.coroutines.android)

    /* ---------------- TESTS ---------------- */
    testApi(libs.junit)
    androidTestApi(libs.mockk.android)
    androidTestApi(libs.androidx.junit.ktx)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
    androidTestApi(platform(libs.androidx.compose.bom))
    androidTestApi(libs.androidx.compose.ui.test.junit4)


    api(libs.androidx.junit.ktx)
}