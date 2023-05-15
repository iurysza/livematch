plugins {
  id("dev.iurysouza.livematch.android-library")
}
android {
  namespace = "dev.iurysouza.livematch.common"
}

dependencies {
  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(libs.squareup.moshi.kotlin)
  kapt(libs.squareup.moshi.kotlinCodegen)

  implementation(platform("androidx.compose:compose-bom:2023.04.01"))
  implementation(libs.androidx.compose.material)
  implementation(libs.bundles.composeMinimal)
  debugImplementation(libs.androidx.compose.ui.uiTooling)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtimeKtx)
  implementation(libs.androidx.lifecycle.viewModelKtx)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.squareup.okhttp.loggingInterceptor)
  implementation(libs.bundles.arrow)
}
