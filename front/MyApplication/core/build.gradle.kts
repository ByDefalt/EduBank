plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "defalt.core"
    compileSdk {
        version = release(36)
    }

    defaultConfig {

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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /* ---------------- COROUTINES ---------------- */
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    /* ---------------- NETWORK (Retrofit 3 / JSON) ---------------- */
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.converter.scalars)


    /* ---------------- DATASTORE ---------------- */
    implementation(libs.androidx.datastore.preferences)


    /* ---------------- DEPENDENCY INJECTION (KOIN) ---------------- */
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
}