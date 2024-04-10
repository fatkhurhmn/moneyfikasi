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
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
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
