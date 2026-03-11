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
// ─── Rapport agrégé des résultats de tests ───────────────────────────────────
// ─── Rapport agrégé des résultats de tests ───────────────────────────────────
tasks.register("testFullReport") {
    group = "Reporting"
    description = "Agrège les résultats XML de tous les modules et affiche un résumé."

    // Résoudre les chemins au moment de la configuration (pas dans doLast)
    val xmlFiles = subprojects.flatMap { sub ->
        listOf(
            "test-results/testDebugUnitTest",
            "test-results/testReleaseUnitTest",
            "test-results/test",
        ).map { sub.layout.buildDirectory.dir(it) }
    }

    // Rendre les inputs déclarés pour le config cache
    inputs.files(xmlFiles).withPropertyName("testResultDirs").optional(true)

    dependsOn(
        subprojects.flatMap { sub ->
            listOf(
                sub.tasks.findByName("testDebugUnitTest"),
                sub.tasks.findByName("test"),
            ).filterNotNull()
        }
    )

    doLast {
        var total = 0; var passed = 0; var failed = 0; var skipped = 0; var errors = 0
        val failedTests = mutableListOf<String>()

        xmlFiles.forEach { dirProvider ->
            val dir = dirProvider.get().asFile
            if (dir.exists()) {
                dir.walk().filter { it.name.startsWith("TEST-") && it.extension == "xml" }.forEach { xmlFile ->
                    val root = groovy.xml.XmlParser().parse(xmlFile)
                    val t = (root.attribute("tests")    as? String)?.toIntOrNull() ?: 0
                    val f = (root.attribute("failures") as? String)?.toIntOrNull() ?: 0
                    val e = (root.attribute("errors")   as? String)?.toIntOrNull() ?: 0
                    val s = (root.attribute("skipped")  as? String)?.toIntOrNull() ?: 0
                    total   += t; failed += f; errors += e; skipped += s

                    // Lister les tests en échec
                    if (f > 0 || e > 0) {
                        (root["testcase"] as? groovy.util.NodeList)?.forEach { tc ->
                            val node = tc as? groovy.util.Node ?: return@forEach
                            val hasFailure = node.children().any { child ->
                                child is groovy.util.Node && child.name() in listOf("failure", "error")
                            }
                            if (hasFailure) {
                                val cls  = node.attribute("classname") as? String ?: "?"
                                val name = node.attribute("name")      as? String ?: "?"
                                failedTests.add("  ❌ $cls#$name")
                            }
                        }
                    }
                }
            }
        }

        passed = total - failed - errors - skipped

        println("""
            ╔══════════════════════════════════╗
            ║       TEST SUMMARY (ALL)         ║
            ╠══════════════════════════════════╣
            ║  Total    : $total
            ║  ✅ Passed : $passed
            ║  ❌ Failed : ${failed + errors}
            ║  ⏭ Skipped: $skipped
            ╚══════════════════════════════════╝
        """.trimIndent())

        if (failedTests.isNotEmpty()) {
            println("\nTests en échec :")
            failedTests.forEach { println(it) }
        }
    }
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


