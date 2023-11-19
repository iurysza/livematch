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
  implementation(project(ProjectModule.WebViewToNativePlayer))
  implementation(project(ProjectModule.MatchThread))
  implementation(project(ProjectModule.MatchDay))
  implementation(project(ProjectModule.DesignSystem))

  implementation(libs.kotlinx.collections.immutable)

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.appcompat)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.androidx.compose.material3)
  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(libs.coil.compose)
  implementation(libs.coil.svg)
}
