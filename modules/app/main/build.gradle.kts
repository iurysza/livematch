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
  implementation(project(ProjectModule.MatchThread))
  implementation(project(ProjectModule.MatchDay))
  implementation(project(ProjectModule.DesignSystem))
  implementation(project(ProjectModule.Common))

  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.appcompat)
  debugImplementation(libs.androidx.compose.ui.uiTooling)

  implementation(libs.google.dagger.hilt.android)
  kapt(libs.google.dagger.hilt.androidCompiler)

  implementation(libs.coil.compose)
  implementation(libs.coil.svg)
}

fun getLocalProperty(key: String) = gradleLocalProperties(rootDir).getProperty(key)
