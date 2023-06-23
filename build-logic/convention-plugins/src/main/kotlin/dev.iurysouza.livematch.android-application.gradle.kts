@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

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
  }
  composeOptions {
    kotlinCompilerExtensionVersion = Versions.Lib.kotlinCompilerExtensionVersion
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
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.jakewharton.timber:timber:${Versions.Lib.timber}")
  implementation("dev.olshevski.navigation:reimagined:${Versions.Lib.reimaginedHilt}")
}
