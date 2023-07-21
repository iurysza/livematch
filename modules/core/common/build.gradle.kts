@file:Suppress("LocalVariableName")

plugins {
  id("dev.iurysouza.livematch.android-library")
}
android {
  namespace = "dev.iurysouza.livematch.common"
  buildTypes {
    val LOCAL_SERVER_DELAY: String by project
    val USE_MOCK_URL: String by project
    getByName("release") {
      buildConfigField(
        type = "Integer",
        name = "LOCAL_SERVER_DELAY",
        value = LOCAL_SERVER_DELAY,
      )
      buildConfigField(
        type = "Boolean",
        name = "USE_MOCK_URL",
        value = false.toString(),
      )
    }
    getByName("debug") {
      buildConfigField(
        type = "Integer",
        name = "LOCAL_SERVER_DELAY",
        value = LOCAL_SERVER_DELAY,
      )
      buildConfigField(
        type = "Boolean",
        name = "USE_MOCK_URL",
        value = USE_MOCK_URL,
      )
    }
  }
}

dependencies {
  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(libs.squareup.moshi.kotlin)
  kapt(libs.squareup.moshi.kotlinCodegen)

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.compose.material)
  implementation(libs.bundles.composeMinimal)
  debugImplementation(libs.androidx.compose.ui.uiTooling)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtimeKtx)
  implementation(libs.androidx.lifecycle.viewModelKtx)

  implementation(libs.squareup.okhttp.loggingInterceptor)
  implementation(libs.bundles.arrow)
}
