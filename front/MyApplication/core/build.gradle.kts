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
    namespace = "defalt.core"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }

    val API_GATEWAY = "\"https://api.example.com/\""



    defaultConfig {
        minSdk = 26

        buildConfigField("String", "GATEWAY_URL", API_GATEWAY)
    }

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "GATEWAY_URL", API_GATEWAY)

        }
        getByName("release") {
            buildConfigField("String", "GATEWAY_URL", API_GATEWAY)
        }
    }
}

dependencies {


    /* ---------------- CORE ---------------- */
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.activity.compose)
    api(libs.kotlin.stdlib)
    api(libs.androidx.junit.ktx)

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

    /* ---------------- COROUTINES ---------------- */
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    testApi(libs.kotlinx.coroutines.test)

    /* ---------------- NETWORK (Retrofit 3 / JSON) ---------------- */
    api(libs.retrofit)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.converter.scalars)

    /* ---------------- DATABASE ---------------- */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    /* ---------------- DATASTORE ---------------- */
    implementation(libs.androidx.datastore.preferences)

    /* ---------------- DEPENDENCY INJECTION (KOIN) ---------------- */
    api(libs.koin.core)
    api(libs.koin.android)
    api(libs.koin.compose)

    /* ---------------- IMAGES ---------------- */
    api(libs.coil.compose)

    /* ---------------- PERMISSIONS ---------------- */
    implementation(libs.accompanist.permissions)

    /* ---------------- SPLASHSCREEN ---------------- */
    api(libs.androidx.core.splashscreen)

    /* ---------------- TESTS ---------------- */
    testApi(libs.junit)
    testApi(libs.kotlintest.runner)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
    androidTestApi(platform(libs.androidx.compose.bom))
    androidTestApi(libs.androidx.compose.ui.test.junit4)

}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.49.0")
    }
    format("misc") {
        target("**/*.gradle", "**/*.md")
        trimTrailingWhitespace()
        endWithNewline()
    }
}