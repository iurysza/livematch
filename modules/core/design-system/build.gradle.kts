@file:Suppress("SpellCheckingInspection")

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

group = "dev.iurysouza.livematch"

android {
  namespace = "dev.iurysouza.livematch.designsystem"
  compileSdk = 33

  defaultConfig {
    minSdk = 26
    targetSdk = 33
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
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.3.2"
  }

  kotlinOptions {
    // Treat all Kotlin warnings as errors (disabled by default)
    allWarningsAsErrors = properties["warningsAsErrors"] as? Boolean ?: false

    freeCompilerArgs = freeCompilerArgs + listOf(
      "-opt-in=kotlin.RequiresOptIn",
      // Enable experimental coroutines APIs, including Flow
      "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
      "-opt-in=kotlinx.coroutines.FlowPreview",
      "-opt-in=kotlin.Experimental",
      // Enable experimental compose APIs
      "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
      "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
      "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
    )
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

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of("11"))
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation(platform("androidx.compose:compose-bom:2022.11.00"))
  implementation("androidx.compose.material:material")
  implementation(libs.bundles.composeMinimal)
  debugImplementation(libs.androidx.compose.ui.uiTooling)
  implementation(libs.androidx.compose.material)
  implementation(libs.androidx.core.ktx)

  implementation(libs.google.acompanist.systemuicontroller)
  implementation(libs.halilibo.composeRichttext.richtextCommonmark)
  implementation("io.coil-kt:coil-compose:2.2.2")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
  implementation("com.jakewharton.timber:timber:5.0.1")
}
