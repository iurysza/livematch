@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

plugins {
  id("dev.iurysouza.livematch.android-library-nokapt")
}

android {
  namespace = "dev.iurysouza.livematch.designsystem"
  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.compose.material)
  debugImplementation(libs.androidx.compose.ui.uiTooling)
  implementation(libs.androidx.core.ktx)

  implementation(libs.lottieCompose)
  implementation(libs.google.acompanist.placeholder.material)
  implementation(libs.google.acompanist.swiperefresh)
  implementation(libs.google.acompanist.systemuicontroller)
  implementation(libs.halilibo.composeRichttext.richtextCommonmark)
  implementation(libs.coil.compose)
}
