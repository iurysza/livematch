@file:Suppress("LocalVariableName")

plugins {
  id("dev.iurysouza.livematch.android-library")
}

android {
  namespace = "dev.iurysouza.livematch.reddit"
  buildTypes {
    val USE_MOCK_URL: String by project
    val REDDIT_API_BASE_URL: String by project
    val MOCK_API_URL: String by project

    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
      buildConfigField(
        type = "String",
        name = "API_URL",
        value = REDDIT_API_BASE_URL,
      )
    }
    getByName("debug") {
      buildConfigField(
        type = "String",
        name = "API_URL",
        value = if (USE_MOCK_URL.toBoolean()) MOCK_API_URL else REDDIT_API_BASE_URL,
      )
    }
  }
}

dependencies {
  implementation(project(":core:common"))

  implementation(libs.bundles.arrow)

  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(libs.squareup.retrofit.core)
  implementation(libs.squareup.retrofit.moshi.converter)

  implementation(libs.squareup.moshi.kotlin)
  implementation(libs.squareup.moshi.moshiAdapters)
  kapt(libs.squareup.moshi.kotlinCodegen)
  testImplementation(libs.squareup.okhttp.mockwebserver)
}
