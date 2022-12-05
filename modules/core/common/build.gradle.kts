plugins {
    id("dev.iurysouza.livematch.android-library")
}
android {
    namespace = "dev.iurysouza.livematch.common"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.google.dagger.hilt.android)
    kapt(libs.google.dagger.hilt.androidCompiler)
    implementation(libs.squareup.moshi.kotlin)
    implementation(libs.squareup.moshi.moshiAdapters)
    kapt(libs.squareup.moshi.kotlinCodegen)

    implementation(libs.bundles.arrow)
}

