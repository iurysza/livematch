@file:Suppress("UnstableApiUsage")

plugins {
  id("dev.iurysouza.livematch.android-library")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "dev.iurysouza.livematch.webviewtonativeplayer"

  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(project(ProjectModule.Common))

  implementation(libs.bundles.arrow)
  implementation(libs.google.dagger.hilt.android)
  implementation("androidx.appcompat:appcompat:1.6.1")
  implementation("com.google.android.material:material:1.10.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.4")
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(platform(libs.androidx.compose.bom))

  implementation(libs.androidx.compose.material3)
  implementation(libs.bundles.composeMinimal)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtimeKtx)
  implementation(libs.androidx.lifecycle.viewModelKtx)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.hilt.navigation.compose)

  implementation(libs.google.acompanist.systemuicontroller)
  implementation(libs.google.acompanist.navigation.animation)

  val libVersionAndroidxMedia3 = "1.1.1"
  implementation("androidx.media3:media3-common:$libVersionAndroidxMedia3")
  implementation("androidx.media3:media3-datasource:$libVersionAndroidxMedia3")
  implementation("androidx.media3:media3-exoplayer:$libVersionAndroidxMedia3")
  implementation("androidx.media3:media3-exoplayer-dash:$libVersionAndroidxMedia3")
  implementation("androidx.media3:media3-exoplayer-hls:$libVersionAndroidxMedia3")
  implementation("androidx.media3:media3-exoplayer-smoothstreaming:$libVersionAndroidxMedia3")
  implementation("androidx.media3:media3-ui:$libVersionAndroidxMedia3")

}
