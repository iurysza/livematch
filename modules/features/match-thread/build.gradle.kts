plugins {
    id("dev.iurysouza.livematch.android-library")
}

android {
    namespace = "dev.iurysouza.livematch.matchthread"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:footballdata"))
    implementation(project(":core:design-system"))
    implementation(project(":core:reddit"))

    implementation(libs.bundles.arrow)
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.androidCompiler)

    implementation(platform("androidx.compose:compose-bom:2022.11.00"))
    implementation("androidx.compose.material:material")
    implementation(libs.bundles.composeMinimal)
    debugImplementation(libs.androidx.compose.ui.uiTooling)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.androidx.lifecycle.viewModelKtx)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.google.acompanist.systemuicontroller)
    implementation(libs.google.acompanist.navigation.animation)

    implementation(libs.squareup.moshi.kotlin)
    kapt(libs.squareup.moshi.kotlinCodegen)

    implementation(libs.halilibo.composeRichttext.richtextCommonmark)
    implementation(libs.coil.compose)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.browser:browser:1.4.0")
}
