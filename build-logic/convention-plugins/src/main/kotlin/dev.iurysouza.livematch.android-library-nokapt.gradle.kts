@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
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

  testImplementation(findLibraryAlias("kotlinx.coroutines.test"))
  testImplementation(findLibraryAlias("kotest.extensions.kotest.assertions.arrow"))
  testImplementation(findLibraryAlias("kotest.kotest.assertions.core"))
  testImplementation(findLibraryAlias("kotest.kotest.runner.junit5"))
  testImplementation(findLibraryAlias("androidx.test.runner"))
  testImplementation(findLibraryAlias("kotlin.reflect"))
}
