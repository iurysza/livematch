import org.gradle.api.JavaVersion

object Versions {

  const val composeKotlinCompilerExtensionVersion = "1.4.7"
  const val detektComposePlugin = "0.1.5"
  val javaTarget = JavaVersion.VERSION_18

  object Android {
    const val compileSdk = 33
    const val minSdk = 26
    const val targetSdk = 33
    const val versionCode = 1
    const val versionName = "0.1.0"
  }
}
