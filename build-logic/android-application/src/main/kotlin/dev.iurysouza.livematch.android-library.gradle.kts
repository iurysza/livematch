@file:Suppress("SpellCheckingInspection", "UnstableApiUsage")

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("dagger.hilt.android.plugin")
  id("kotlin-parcelize")
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
  id("kotlin-kapt")
}

android {
  compileSdk = Versions.Android.compileSdk

  defaultConfig {
    minSdk = Versions.Android.minSdk
    targetSdk = Versions.Android.targetSdk
  }
  hilt {
    enableAggregatingTask = true
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
    kotlinCompilerExtensionVersion = Versions.Lib.kotlinCompilerExtensionVersion
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
    languageVersion.set(JavaLanguageVersion.of(8))
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.jakewharton.timber:timber:${Versions.Lib.timber}")
  implementation("dev.olshevski.navigation:reimagined-hilt:${Versions.Lib.reimaginedHilt}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Lib.kotlinxCoroutinesAndroid}")

  testImplementation("androidx.test:runner:${Versions.Lib.testRunner}")
  testImplementation("io.kotest.extensions:kotest-assertions-arrow:${Versions.Lib.kotestAssertionsArrow}")
  testImplementation("io.kotest:kotest-assertions-core-jvm:${Versions.Lib.kotestRunnerJunit5Jvm}")
  testImplementation("io.kotest:kotest-runner-junit5-jvm:${Versions.Lib.kotestRunnerJunit5Jvm}")
  testImplementation("org.jetbrains.kotlin:kotlin-reflect:${Versions.Lib.kotlinReflect}}")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.Lib.kotlinxCoroutinesAndroid}")
}
