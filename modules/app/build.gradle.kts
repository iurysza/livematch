@file:Suppress("LocalVariableName")

plugins {
    id("dev.iurysouza.livematch.android-application")
}

android {
    buildTypes {
        val USE_MOCK_URL: String by project
        val API_URL: String by project
        val MOCK_API_URL: String by project

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_URL", API_URL)
        }
        getByName("debug") {
            buildConfigField(
                type = "String",
                name = "API_URL",
                value = if (USE_MOCK_URL.toBoolean()) MOCK_API_URL else API_URL)
        }
    }
}
dependencies {
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit.core)

    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation("com.google.accompanist:accompanist-navigation-animation:0.21.5-rc")
    implementation(("com.google.accompanist:accompanist-systemuicontroller:0.21.5-rc"))
    implementation(("com.squareup.moshi:moshi-kotlin:1.12.0"))
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")

    testImplementation("androidx.test:runner:1.4.0")
    testImplementation("io.mockk:mockk:1.12.1")
    testImplementation("io.mockk:mockk-android:1.12.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("io.kotest:kotest-assertions-core:4.6.3")
}
