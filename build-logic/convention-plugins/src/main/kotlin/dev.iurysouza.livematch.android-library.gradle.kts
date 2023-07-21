@file:Suppress("SpellCheckingInspection", "UnstableApiUsage", "LocalVariableName")

plugins {
  id("com.android.library")
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
    minSdk = Versions.Android.minSdk
  }
  hilt {
    enableAggregatingTask = true
  }
  buildFeatures {
    // should enable this only when needed,
    // but I'm currently getting a warning for every moddule that has this disabled, so we're keeping this ON for now
    buildConfig = true
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
    }
  }
  composeOptions {
    kotlinCompilerExtensionVersion = Versions.composeKotlinCompilerExtensionVersion
  }

  kotlinOptions {
    // Treat all Kotlin warnings as errors (disabled by default)
    allWarningsAsErrors = properties["warningsAsErrors"] as? Boolean ?: false
    freeCompilerArgs = freeCompilerArgs + liveMatchCompilerOptions
  }
  testOptions {
    unitTests.all {
      it.useJUnitPlatform()
    }
  }
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(18))
  }
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(findLibraryAlias("timber"))
  implementation(findLibraryAlias("navigationReimagined"))
  implementation(findLibraryAlias("kotlinx.coroutines.android"))
  implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")

  testImplementation(findLibraryAlias("kotlinx.coroutines.test"))
  testImplementation(findLibraryAlias("kotest.extensions.kotest.assertions.arrow"))
  testImplementation(findLibraryAlias("kotest.kotest.assertions.core"))
  testImplementation(findLibraryAlias("kotest.kotest.runner.junit5"))
  testImplementation(findLibraryAlias("androidx.test.runner"))
  testImplementation(findLibraryAlias("kotlin.reflect"))
}
