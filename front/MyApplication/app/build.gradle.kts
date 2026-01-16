import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Gère automatiquement le compilateur Compose
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

    // Suppression de composeOptions { kotlinCompilerExtensionVersion }
    // car géré par le plugin kotlin.compose (Kotlin 2.0+)
}

dependencies {

    implementation(project(":core"))
    implementation(project(":feature-account"))
    implementation(project(":feature-offer"))
    implementation(project(":feature-bank"))
    implementation(project(":feature-operation"))


    /* ---------------- CORE ---------------- */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlin.stdlib)

    /* ---------------- COMPOSE ---------------- */
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    /* ---------------- NAVIGATION ---------------- */
    implementation(libs.androidx.navigation.compose)

    /* ---------------- VIEWMODEL ---------------- */
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

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

    /* ---------------- DATABASE ---------------- */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    /* ---------------- DATASTORE ---------------- */
    implementation(libs.androidx.datastore.preferences)

    /* ---------------- DEPENDENCY INJECTION (KOIN) ---------------- */
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    /* ---------------- IMAGES ---------------- */
    implementation(libs.coil.compose)

    /* ---------------- PERMISSIONS ---------------- */
    implementation(libs.accompanist.permissions)

    /* ---------------- SPLASHSCREEN ---------------- */
    implementation(libs.androidx.core.splashscreen)

    /* ---------------- TESTS ---------------- */
    testImplementation(libs.junit)
    testImplementation(libs.kotlintest.runner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
}

// Configuration Spotless (Optionnel, si vous voulez le configurer ici)
spotless {
    kotlin {
        ktfmt()
    }
}