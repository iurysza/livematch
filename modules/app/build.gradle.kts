@file:Suppress("LocalVariableName")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("dev.iurysouza.livematch.android-application")
}

android {
    namespace = "dev.iurysouza.livematch"
    signingConfigs {
        create("release") {
            keyAlias = getLocalProperty("keyAlias")
            keyPassword = getLocalProperty("keyPassword")
            storePassword = getLocalProperty("storePassword")
            storeFile = file(getLocalProperty("storeFile"))
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
        getByName("debug") {
            isDebuggable = true
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":features:match-thread"))
    implementation(project(":features:match-list"))
    implementation(project(":core:footballdata"))
    implementation(project(":core:reddit"))
    implementation(project(":core:design-system"))
    implementation(project(":core:common"))

    implementation(platform("androidx.compose:compose-bom:2022.11.00"))
    implementation("androidx.compose.material:material")
    implementation(libs.bundles.composeMinimal)

    debugImplementation(libs.androidx.compose.ui.uiTooling)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.androidCompiler)
    implementation(libs.google.acompanist.navigation.animation)
    implementation(libs.bundles.arrow)

    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil-svg:2.2.2")
}

fun getLocalProperty(key: String) = gradleLocalProperties(rootDir).getProperty(key)
