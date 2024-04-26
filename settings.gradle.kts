enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

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
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
include(":feature:settings")
include(":feature:wallet")
include(":shared:utils")
include(":feature:transaction")
include(":feature:statistic")
include(":feature:search")
