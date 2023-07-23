@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

import Versions.Lib

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
  implementation(findLibraryAlias(Lib.Timber))
  implementation(findLibraryAlias(Lib.NavigationReimagined))
  implementation(findLibraryAlias(Lib.CoroutinesAndroid))

  testImplementation(findLibraryAlias(Lib.CoroutinesTest))
  testImplementation(findLibraryAlias(Lib.KotestExtensions))
  testImplementation(findLibraryAlias(Lib.KotestAssertions))
  testImplementation(findLibraryAlias(Lib.KotestRunner))
  testImplementation(findLibraryAlias(Lib.AndroidTestRunner))
  testImplementation(findLibraryAlias(Lib.KotlinReflect))
}
