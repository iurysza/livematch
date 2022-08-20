import com.android.build.gradle.ProguardFiles.getDefaultProguardFile
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

group = "com.example.myproduct"

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.iurysouza.livematch"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        // Treat all Kotlin warnings as errors (disabled by default)
        allWarningsAsErrors = properties["warningsAsErrors"] as? Boolean ?: false

        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=kotlin.RequiresOptIn",
            // Enable experimental coroutines APIs, including Flow
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlin.Experimental",
        )
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += mutableSetOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(kotlin("stdlib"))
    implementation("androidx.compose.material:material:1.1.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
    implementation("androidx.compose.ui:ui:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.navigation:navigation-compose:2.5.1")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.21.5-rc")
    implementation(("com.google.accompanist:accompanist-systemuicontroller:0.21.5-rc"))

    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation(("com.squareup.moshi:moshi-kotlin:1.12.0"))
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.appcompat:appcompat:1.5.0")
    implementation("androidx.core:core-ktx:1.8.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")

    testImplementation("androidx.test:runner:1.4.0")
    testImplementation("io.mockk:mockk:1.12.1")
    testImplementation("io.mockk:mockk-android:1.12.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("io.kotest:kotest-assertions-core:4.6.3")


}
