plugins {
  id("dev.iurysouza.livematch.android-application")
}

android {
  namespace = "dev.iurysouza.livematch.playground"
  defaultConfig {
    applicationId = "dev.iurysouza.livematch.playground"
  }
  buildTypes {
    getByName("debug") {
      isDebuggable = true
    }
  }
}

dependencies {
  implementation(project(":features:match-thread"))
  implementation(project(":features:match-day"))
  implementation(project(":core:design-system"))

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.appcompat)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(libs.coil.compose)
  implementation(libs.coil.svg)
}
