import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    // Ne PAS remettre spotless ici s'il est déjà au niveau root
}

// 1. CONFIGURATION KOTLIN (DOIT ÊTRE EN PREMIER ET HORS DU BLOC ANDROID)
kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

// 2. CONFIGURATION ANDROID
android {
    namespace = "defalt.eduBank"
    compileSdk = 35

    defaultConfig {
        applicationId = "defalt.eduBank"
        minSdk = 24
        targetSdk = 35
        // ...
    }

    // Dans AGP 9+ avec Kotlin 2.x, on ne met PLUS de kotlinOptions { ... }
    // à l'intérieur du bloc android. C'est géré par le bloc kotlin { } ci-dessus.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Projets locaux
    implementation(project(":core"))
    implementation(project(":feature-account"))
    implementation(project(":feature-offer"))
    implementation(project(":feature-bank"))
    implementation(project(":feature-operation"))

    /* ---------------- CORE & COMPOSE ---------------- */
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlin.stdlib)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    /* ---------------- ARCHITECTURE ---------------- */
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    /* ---------------- ASYNC & NETWORK ---------------- */
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlinx.serialization)
    implementation(libs.converter.scalars)

    /* ---------------- DATABASE & DI ---------------- */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    /* ---------------- UI EXTRAS ---------------- */
    implementation(libs.coil.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.core.splashscreen)

    /* ---------------- TESTS ---------------- */
    testImplementation(libs.junit)
    testImplementation(libs.kotlintest.runner)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}

spotless {
    kotlin {
        ktfmt()
    }
}