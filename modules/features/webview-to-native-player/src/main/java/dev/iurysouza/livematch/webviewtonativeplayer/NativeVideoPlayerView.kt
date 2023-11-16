package dev.iurysouza.livematch.webviewtonativeplayer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import dev.iurysouza.livematch.webviewtonativeplayer.detector.BackgroundVideoLoader
import dev.iurysouza.livematch.webviewtonativeplayer.detector.OkHTTPVideoLoader
import dev.iurysouza.livematch.webviewtonativeplayer.player.NativeVideoPlayer
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("ViewConstructor", "UnsafeOptInUsageError")
class NativeVideoPlayerView(
  private val initUrl: String,
  context: Context,
  attrs: AttributeSet? = null,
) : FrameLayout(context, attrs), LifecycleEventObserver {

  private var player: NativeVideoPlayer? = null
  private val activity by lazy { context.getActivity() as? ComponentActivity? }
  private var backgroundVideoLoader: BackgroundVideoLoader = OkHTTPVideoLoader()
  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    Timber.v("NativeVideo LifecycleObserver: $event")
    when (event) {
      Lifecycle.Event.ON_PAUSE -> player?.pause()
      Lifecycle.Event.ON_RESUME -> player?.play()
      else -> { /* do nothing */
      }
    }
  }

  init {
    LayoutInflater.from(context).inflate(R.layout.native_video_player_view_activity, this, true)
    player = NativeVideoPlayer(findViewById(R.id.player_view))
  }


  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    activity?.lifecycle?.addObserver(this)
    activity?.lifecycleScope?.launch {
      backgroundVideoLoader.fetchVideoFileFromPage(initUrl)?.let { videoUrl ->
        player?.playVideo(videoUrl)
      }
    }
  }

  @UnstableApi
  override fun
    onDetachedFromWindow() {
    super.onDetachedFromWindow()
    player?.onRelease()
    activity?.lifecycle?.removeObserver(this)

  }

  private fun Context?.getActivity(): Activity? {
    var c = this
    while (c is ContextWrapper) {
      if (c is Activity) {
        return c
      }
      c = c.baseContext
    }
    return null
  }
}
