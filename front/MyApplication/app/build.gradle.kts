import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    jacoco
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
        debug {
            enableAndroidTestCoverage = true
            enableUnitTestCoverage = true
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(project(":core:testing"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))
    implementation(project(":core:domain"))
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

// ─── JaCoCo ───────────────────────────────────────────────────────────────────

jacoco {
    toolVersion = "0.8.11"
}

val jacocoExcludes = listOf(
    // Android / Build
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "android/**/*.*",
    // Jetpack Compose
    "**/*ComposableSingletons*",
    "**/*_PreviewParameterProvider*",
    "**/*Preview*",
    // Koin DI
    "**/di/**",
    "**/*Module*",
    // Tests
    "**/*Test*.*",
    "**/test/**",
    "**/androidTest/**",
)

// ── Rapport pour le module :app uniquement ────────────────────────────────────
tasks.register<JacocoReport>("jacocoTestReport") {
    group = "Reporting"
    description = "Génère le rapport de couverture JaCoCo pour le module :app (debug)."

    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/jacocoTestReport.xml"))
    }

    // AGP génère les .class dans ces deux emplacements selon la version
    val kotlinClasses = fileTree(layout.buildDirectory.dir("tmp/kotlin-classes/debug")) {
        exclude(jacocoExcludes)
    }
    val javacClasses = fileTree(layout.buildDirectory.dir("intermediates/javac/debug")) {
        exclude(jacocoExcludes)
    }
    classDirectories.setFrom(kotlinClasses, javacClasses)

    sourceDirectories.setFrom(
        files(
            "${projectDir}/src/main/java",
            "${projectDir}/src/main/kotlin",
        )
    )
    executionData.setFrom(
        fileTree(layout.buildDirectory) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "jacoco/testDebugUnitTest.exec",
            )
        }
    )
}