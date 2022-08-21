enableFeaturePreview("VERSION_CATALOGS")
// == Define locations for build logic ==
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
    includeBuild("../build-logic")
}

// == Define locations for components ==
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
    repositories {
        mavenCentral()
        google()
    }
}

// == Define the inner structure of this component ==
rootProject.name = "modules"
include("app")
