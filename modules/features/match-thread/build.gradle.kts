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
  implementation(project(":core:common"))
  implementation(project(":core:footballdata"))
  implementation(project(":core:design-system"))
  implementation(project(":core:reddit"))

  implementation(libs.bundles.arrow)
  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(platform("androidx.compose:compose-bom:2023.04.01"))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.compose.material)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.squareup.moshi.kotlin)
  kapt(libs.squareup.moshi.kotlinCodegen)

  implementation(libs.halilibo.composeRichttext.richtextCommonmark)
  implementation(libs.coil.compose)
  implementation(libs.androidx.constraintlayout.constraintlayoutCompose)
  implementation(libs.androidx.browser.browser)
}
