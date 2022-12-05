plugins {
    id("dev.iurysouza.livematch.android-library")
}
android {
    namespace = "dev.iurysouza.livematch.common"
}

dependencies {
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.androidCompiler)

    implementation(libs.squareup.moshi.kotlin)
    kapt(libs.squareup.moshi.kotlinCodegen)

    implementation(libs.squareup.okhttp.loggingInterceptor)
    implementation(libs.bundles.arrow)
}

