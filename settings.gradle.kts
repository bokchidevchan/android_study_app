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

rootProject.name = "Android-Study-App"

include(":app")

// Core modules
include(":core:core-common")
include(":core:core-network")
include(":core:core-ui")
include(":core:core-testing")

// Domain modules
include(":domain:domain-market")

// Data modules
include(":data:data-market")

// Feature modules
include(":feature:feature-market")
