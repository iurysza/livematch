@file:Suppress("SpellCheckingInspection")

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

group = "dev.iurysouza.livematch"

android {
  namespace = "dev.iurysouza.livematch.designsystem"
  compileSdk = Versions.Android.compileSdk
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
  }
  defaultConfig {
    minSdk = Versions.Android.minSdk
  }

  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = Versions.Lib.composeKotlinCompilerExtensionVersion
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
tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.bundles.composeMinimal)
  implementation(libs.androidx.compose.material)
  debugImplementation(libs.androidx.compose.ui.uiTooling)
  implementation(libs.androidx.core.ktx)

  implementation(libs.google.acompanist.systemuicontroller)
  implementation(libs.halilibo.composeRichttext.richtextCommonmark)
  implementation(libs.coil.compose)
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
  implementation("com.jakewharton.timber:timber:5.0.1")
}
kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(18))
  }
}
