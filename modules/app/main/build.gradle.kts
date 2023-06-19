@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("dev.iurysouza.livematch.android-application")
}

android {
  namespace = "dev.iurysouza.livematch"
  signingConfigs {
    create("release") {
      keyAlias = getLocalProperty("keyAlias")
      keyPassword = getLocalProperty("keyPassword")
      storePassword = getLocalProperty("storePassword")
      storeFile = file(getLocalProperty("storeFile"))
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("release")
      isDebuggable = false
    }
    getByName("debug") {
      isDebuggable = true
      applicationIdSuffix = ".debug"
    }
  }
}

dependencies {
  implementation(project(":features:match-thread"))
  implementation(project(":features:match-day"))
  implementation(project(":core:design-system"))
  implementation(project(":core:common"))

  implementation(platform("androidx.compose:compose-bom:2023.04.01"))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.appcompat)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(libs.coil.compose)
  implementation(libs.coil.svg)
}

fun getLocalProperty(key: String) = gradleLocalProperties(rootDir).getProperty(key)
