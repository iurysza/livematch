@file:Suppress("UnstableApiUsage")

plugins {
  id("dev.iurysouza.livematch.android-library")
}

android {
  namespace = "dev.iurysouza.livematch.matchday"

  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(project(ProjectModule.Common))
  implementation(project(ProjectModule.FootballInfo))
  implementation(project(ProjectModule.FootballData))
  implementation(project(ProjectModule.DesignSystem))
  implementation(project(ProjectModule.Reddit))

  implementation(libs.bundles.arrow)
  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(platform(libs.androidx.compose.bom))

  implementation(libs.androidx.compose.material)
  implementation(libs.bundles.composeMinimal)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtimeKtx)
  implementation(libs.androidx.lifecycle.viewModelKtx)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.hilt.navigation.compose)

  implementation(libs.google.acompanist.placeholder.material)
  implementation(libs.google.acompanist.systemuicontroller)
  implementation(libs.google.acompanist.navigation.animation)
  implementation(libs.squareup.moshi.kotlin)
  kapt(libs.squareup.moshi.kotlinCodegen)

  implementation(libs.coil.compose)
}
