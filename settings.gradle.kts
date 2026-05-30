enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

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
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "moneyfikasi"
include(":app")
include(":shared:resource")
include(":shared:common-ui")
include(":shared:navigation")
include(":shared:data")
include(":shared:domain")
include(":feature:category")
include(":feature:more")
include(":feature:wallet")
include(":shared:utils")
include(":feature:transaction")
include(":feature:statistic")
include(":feature:search")
include(":feature:backup_restore")
include(":feature:home")
include(":feature:preset")
include(":feature:budget")
include(":feature:export")
include(":feature:applock")
include(":feature:settings")
include(":feature:recurring_transaction")
include(":feature:notification")
include(":feature:about")
