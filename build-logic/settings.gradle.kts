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
include("convention-plugins")
