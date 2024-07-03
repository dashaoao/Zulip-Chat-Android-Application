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
    }
}
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "coursework"
include(":app")
include(":common:core")
include(":common:util")
include(":common:ui")
include(":common:data")
include(":features:users:data")
include(":features:users:domain")
include(":features:users:presentation")
include(":features:channels:presentation")
include(":features:channels:data")
include(":features:channels:domain")
include(":features:chat:domain")
include(":features:chat:data")
include(":features:chat:presentation")
include(":test")
