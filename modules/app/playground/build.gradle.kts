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
  implementation(project(":core:common"))

  implementation(platform("androidx.compose:compose-bom:2023.04.01"))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.appcompat)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(libs.coil.compose)
  implementation(libs.coil.svg)
}
