@file:Suppress("LocalVariableName", "UnstableApiUsage")

plugins {
  id("dev.iurysouza.livematch.android-library")
}

android {
  namespace = "dev.iurysouza.livematch.footballinfo"
  buildTypes {
    val USE_MOCK_URL: String by project
    val FOOTBALL_INFO_BASE_URL: String by project

    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
      buildConfigField(
        type = "String",
        name = "FOOTBALL_INFO_BASE_URL",
        value = FOOTBALL_INFO_BASE_URL,
      )
    }
    getByName("debug") {
      buildConfigField(
        type = "String",
        name = "FOOTBALL_INFO_BASE_URL",
        value = if (USE_MOCK_URL.toBoolean()) getMockUrlBasedOnDeviceType(project) else FOOTBALL_INFO_BASE_URL,
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
  kapt(libs.squareup.moshi.kotlinCodegen)
}
