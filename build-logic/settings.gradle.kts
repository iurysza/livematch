enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
    repositories {
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "build-logic"
include("android-application")
