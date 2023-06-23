val liveMatchCompilerOptions = listOf(
  "-opt-in=kotlin.RequiresOptIn",
// Enable experimental coroutines APIs, including Flow
  "-opt-in=kotlin.Experimental",
  "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
  "-opt-in=kotlinx.coroutines.FlowPreview",
// Enable experimental compose APIs
  "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
  "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
  "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
)
