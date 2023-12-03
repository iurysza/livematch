@file:Suppress("UnstableApiUsage")

import java.net.URI.create

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
    maven { url = create("https://oss.sonatype.org/content/repositories/snapshots/") }
    maven { url = create("https://jitpack.io") }
    google()
  }
}

// == Define the inner structure of this component ==
rootProject.name = "modules"
include("core:reddit")
include("core:footballinfo")
include("core:design-system")
include("core:common")
include("features:match-thread")
include("core:webview-to-native-player")
include("features:match-day")
include("app:playground")
include("app:main")
