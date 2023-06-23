plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(libs.secrets.gradlePlugin)
  implementation(libs.android.gradlePlugin)
  implementation(libs.kotlin.gradlePlugin)
  implementation(libs.ktlint.gradlePlugin)
  implementation(libs.detekt.gradlePlugin)
  implementation(libs.hilt.gradlePlugin)
}
