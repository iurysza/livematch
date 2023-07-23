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
  sealed class Lib(val alias: String) {
    object Timber : Lib("timber")
    object NavigationReimagined : Lib("navigationReimagined")
    object CoroutinesAndroid : Lib("kotlinx.coroutines.android")
    object CoroutinesTest : Lib("kotlinx.coroutines.test")
    object KotestExtensions : Lib("kotest.extensions.kotest.assertions.arrow")
    object KotestAssertions : Lib("kotest.kotest.assertions.core")
    object KotestRunner : Lib("kotest.kotest.runner.junit5")
    object AndroidTestRunner : Lib("androidx.test.runner")
    object KotlinReflect : Lib("kotlin.reflect")
    object ImmutableCollections : Lib("kotlinx.collections.immutable")
  }

}
