plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.spotless)
    jacoco
}

val jacocoExcludes = listOf(
    // Android / Build
    "**/R.class", "**/R$*.class",
    "**/BuildConfig.*", "**/Manifest*.*",
    "android/**/*.*",

    // Compose générés
    "**/*ComposableSingletons*",
    "**/*Preview*",
    "**/*\$*Preview*",

    // Lambdas et classes anonymes Kotlin/Compose
    "**/*\$\$*",                          // lambdas inlinées $$inlined
    "**/*\$Lambda*",                      // Function4, Lambda générés
    "**/*\$inlined*",
    "**/*\$sam\$*",
    "**/*\$WhenMappings*",

    // LazyDsl / Compose runtime internals
    "**/LazyDsl*",
    "**/ComposableLambda*",
    "**/ComposedModifier*",
    "**/SnapshotState*",
    "**/remember*",

    // DI
    "**/di/**",
    "**/*Module*",
    "**/*_Factory*",
    "**/*_HiltComponents*",
    "**/*Hilt_*",

    // Tests
    "**/*Test*.*",
    "**/test/**",
    "**/androidTest/**",

    // Navigation générés
    "**/*Directions*",
    "**/*Args*",

    //Screen et UI
    "**/ui/**",
    "**/infrastructure/**",
    "**/eduBank/**",
)

tasks.register<JacocoReport>("jacocoFullReport") {
    group = "Reporting"
    description = "Génère le rapport de couverture JaCoCo agrégé pour tous les modules."

    dependsOn(
        subprojects.flatMap { sub ->
            listOfNotNull(
                sub.tasks.findByName("testDebugUnitTest"),
                sub.tasks.findByName("test"),
            )
        }
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/full/html"))
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/full/jacocoFullReport.xml"))
    }

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
                    "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                    "jacoco/testDebugUnitTest.exec",
                    "jacoco/test.exec",
                )
            }.files
        }
    )
}


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
        listOfNotNull(
            sub.tasks.findByName("testDebugUnitTest"),
            sub.tasks.findByName("test"),
        )
    }.filterIsInstance<AbstractTestTask>()

    dependsOn(testTasks)

    destinationDirectory.set(layout.buildDirectory.dir("reports/tests/full"))

    testResults.setFrom(testTasks.map { it.binaryResultsDirectory })
}


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


