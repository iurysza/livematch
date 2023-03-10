@file:Suppress("SpellCheckingInspection")

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("dagger.hilt.android.plugin")
  id("kotlin-parcelize")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
  id("kotlin-kapt")
}

group = "dev.iurysouza.livematch"

android {
  compileSdk = 33

  defaultConfig {
    minSdk = 26
    targetSdk = 33
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
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
  implementation("com.jakewharton.timber:timber:5.0.1")

//    testImplementation(libs.kotlin.reflect)
  testImplementation("org.jetbrains.kotlin:kotlin-reflect:1.7.21")
//    testImplementation(libs.bundles.kotestBundle)
  testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
  testImplementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
  testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.3.0")
//    testImplementation(libs.androidx.test.runner)
  testImplementation("androidx.test:runner:1.5.1")
//    testImplementation(libs.kotlinx.coroutines.test)
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}
