plugins {
    id("dev.iurysouza.livematch.android-application")
}

android {
    namespace = "dev.iurysouza.livematch.playground"
    defaultConfig {
        applicationId = "dev.iurysouza.livematch.playground"
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
        }
    }
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
    implementation(libs.androidx.appcompat)

    debugImplementation(libs.androidx.compose.ui.uiTooling)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.androidCompiler)
    implementation(libs.google.acompanist.navigation.animation)
    implementation(libs.bundles.arrow)

  implementation(libs.coil.compose)
  implementation(libs.coil.svg)
}
