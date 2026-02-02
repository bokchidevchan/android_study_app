// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kover)
}

dependencies {
    kover(project(":app"))
    kover(project(":core:core-common"))
    kover(project(":core:core-network"))
    kover(project(":domain:domain-market"))
    kover(project(":data:data-market"))
    kover(project(":feature:feature-market"))
}

kover {
    reports {
        filters {
            excludes {
                classes(
                    // DTOs
                    "*Dto",
                    "*Dto\$*",
                    // DI Modules
                    "*Module*",
                    "*_Factory*",
                    // Build Config
                    "*BuildConfig*",
                    // Theme
                    "*.theme.*",
                    "*.di.*",
                    // Compose generated
                    "*ComposableSingletons*",
                    "*_Factor*",
                    // Android generated
                    "*_Impl*",
                    "*_MembersInjector*"
                )
                packages(
                    "*.di",
                    "*.theme"
                )
            }
        }

        verify {
            rule {
                minBound(80)
            }
        }
    }
}
