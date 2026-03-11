// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.spotless)
    jacoco
}

// ─── Exclusions JaCoCo partagées ─────────────────────────────────────────────
val jacocoExcludes = listOf(
    "**/R.class", "**/R$*.class",
    "**/BuildConfig.*", "**/Manifest*.*",
    "android/**/*.*",
    "**/*ComposableSingletons*", "**/*Preview*",
    "**/di/**", "**/*Module*",
    "**/*Test*.*", "**/test/**", "**/androidTest/**",
)

// ─── Rapport agrégé tous modules ─────────────────────────────────────────────
tasks.register<JacocoReport>("jacocoFullReport") {
    group = "Reporting"
    description = "Génère le rapport de couverture JaCoCo agrégé pour tous les modules."

    // Collect all subproject test tasks (Android + JVM)
    dependsOn(
        subprojects.flatMap { sub ->
            listOf(
                sub.tasks.findByName("testDebugUnitTest"),
                sub.tasks.findByName("test"),
            ).filterNotNull()
        }
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/full/html"))
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/full/jacocoFullReport.xml"))
    }

    // Modules Android : classes dans tmp/kotlin-classes/debug ET intermediates/javac/debug
    // Modules JVM     : classes dans build/classes/kotlin/main
    classDirectories.setFrom(
        subprojects.flatMap { sub ->
            listOf(
                fileTree("${sub.layout.buildDirectory.get()}/tmp/kotlin-classes/debug") { exclude(jacocoExcludes) },
                fileTree("${sub.layout.buildDirectory.get()}/intermediates/javac/debug/classes") { exclude(jacocoExcludes) },
                fileTree("${sub.layout.buildDirectory.get()}/classes/kotlin/main") { exclude(jacocoExcludes) },
            )
        }
    )

    sourceDirectories.setFrom(
        subprojects.flatMap { sub ->
            listOf(
                "${sub.projectDir}/src/main/java",
                "${sub.projectDir}/src/main/kotlin",
            )
        }.map { file(it) }.filter { it.exists() }
    )

    executionData.setFrom(
        subprojects.flatMap { sub ->
            fileTree(sub.layout.buildDirectory.get()) {
                include(
                    // modules Android
                    "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                    "jacoco/testDebugUnitTest.exec",
                    // modules JVM
                    "jacoco/test.exec",
                )
            }.files
        }
    )
}
// ─── Force ignoreFailures pour le rapport agrégé ──────────────────────────────
gradle.taskGraph.whenReady {
    if (hasTask(":testFullReport")) {
        allTasks
            .filterIsInstance<AbstractTestTask>()
            .forEach { it.ignoreFailures = true }
    }
}
tasks.register<TestReport>("testFullReport") {
    group = "Reporting"
    description = "Génère le rapport de tests agrégé pour tous les modules."

    val testTasks = subprojects.flatMap { sub ->
        listOf(
            sub.tasks.findByName("testDebugUnitTest"),
            sub.tasks.findByName("test"),
        ).filterNotNull()
    }.filterIsInstance<AbstractTestTask>()  // ✅ cast pour accéder à binaryResultsDirectory

    dependsOn(testTasks)

    destinationDirectory.set(layout.buildDirectory.dir("reports/tests/full"))

    // ✅ Pointe vers les résultats binaires de chaque tâche, pas les XML
    testResults.setFrom(testTasks.map { it.binaryResultsDirectory })
}

// ─── Spotless ─────────────────────────────────────────────────────────────────
spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.49.0").editorConfigOverride(
            mapOf(
                "ktlint_standard_no-wildcard-imports" to "disabled",
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


