plugins {
    id("dev.iurysouza.livematch.android-application")
}

android{
    buildTypes {
        val USE_MOCK_URL: String by project
        val API_URL: String by project
        val MOCK_API_URL: String by project

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro")
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
dependencies{
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
