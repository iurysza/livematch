@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

import Versions.Lib

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("kotlin-kapt")
  id("kotlin-parcelize")
  id("dagger.hilt.android.plugin")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
  compileSdk = Versions.Android.compileSdk
  compileOptions {
    sourceCompatibility = Versions.javaTarget
    targetCompatibility = Versions.javaTarget
  }
  defaultConfig {
    applicationId = "dev.iurysouza.livematch"
    minSdk = Versions.Android.minSdk
    targetSdk = Versions.Android.targetSdk
    versionCode = Versions.Android.versionCode
    versionName = Versions.Android.versionName

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  kotlinOptions {
    // Treat all Kotlin warnings as errors (disabled by default)
    allWarningsAsErrors = properties["warningsAsErrors"] as? Boolean ?: false
    freeCompilerArgs = freeCompilerArgs + liveMatchCompilerOptions
  }
  buildFeatures {
    compose = true
    // should enable this only when needed,
    // but I'm currently getting a warning for every moddule that has this disabled, so we're keeping this ON for now
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = Versions.composeKotlinCompilerExtensionVersion
  }
  packaging {
    resources {
      excludes += mutableSetOf("/META-INF/{AL2.0,LGPL2.1}")
    }
  }
  hilt {
    enableAggregatingTask = true
  }
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(18))
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(findLibraryAlias(Lib.Timber))
  implementation(findLibraryAlias(Lib.NavigationReimagined))
}
