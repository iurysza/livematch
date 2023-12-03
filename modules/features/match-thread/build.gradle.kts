plugins {
  id("dev.iurysouza.livematch.android-library")
}

android {
  namespace = "dev.iurysouza.livematch.matchthread"

  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(project(ProjectModule.WebViewToNativePlayer))
  implementation(project(ProjectModule.Common))
  implementation(project(ProjectModule.FootballInfo))
  implementation(project(ProjectModule.DesignSystem))
  implementation(project(ProjectModule.Reddit))

  implementation(libs.bundles.arrow)
  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.compose.material3)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.squareup.moshi.kotlin)
  kapt(libs.squareup.moshi.kotlinCodegen)

  implementation(libs.halilibo.composeRichttext.richtextCommonmark)
  implementation(libs.coil.compose)
  implementation(libs.androidx.constraintlayout.constraintlayoutCompose)
  implementation(libs.androidx.browser.browser)
}
