pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "My Application"
include(
    ":app",
    ":feature-account",
    ":feature-bank",
    ":feature-operation",
    ":feature-offer"
)
include(":core:network")
include(":core:ui")
include(":core:domain")
include(":core:utils")

include(":core:database")
include(":core:testing")
include(":core:testing-android")
