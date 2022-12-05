plugins {
    id("dev.iurysouza.livematch.android-library")
}

android {
    namespace = "dev.iurysouza.livematch.designsystem"

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
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2022.11.00"))
    implementation("androidx.compose.material:material")
    implementation(libs.bundles.composeMinimal)
    debugImplementation(libs.androidx.compose.ui.uiTooling)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.androidCompiler)

    implementation(libs.google.acompanist.systemuicontroller)
    implementation(libs.google.acompanist.navigation.animation)

    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation(libs.halilibo.composeRichttext.richtextCommonmark)

    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("io.coil-kt:coil-svg:2.2.2")

    testImplementation(libs.squareup.okhttp.mockwebserver)
    testImplementation(libs.kotlin.reflect)
    testImplementation(libs.bundles.kotestBundle)
    testImplementation(libs.androidx.test.runner)
    testImplementation(libs.kotlinx.coroutines.test)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.browser:browser:1.4.0")
}
