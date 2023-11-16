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
import dev.iurysouza.livematch.webviewtonativeplayer.player.NativeVideoPlayer
import dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper.RedditVideoUriScrapper
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("ViewConstructor", "UnsafeOptInUsageError")
class NativeVideoPlayerView(
  private val pageUrl: String,
  context: Context,
  attrs: AttributeSet? = null,
) : FrameLayout(context, attrs), LifecycleEventObserver {

  private val activity by lazy { context.getActivity() }
  private val videoUriExtractor by lazy { RedditVideoUriScrapper() }
  private val player by lazy { NativeVideoPlayer(findViewById(R.id.player_view)) }

  init {
    LayoutInflater.from(context).inflate(R.layout.native_video_player_view_activity, this, true)
  }

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    Timber.v("NativeVideo LifecycleObserver: $event")
    when (event) {
      Lifecycle.Event.ON_PAUSE -> player.pause()
      Lifecycle.Event.ON_RESUME -> player.play()
      else -> { /* do nothing */
      }
    }
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    activity?.lifecycle?.addObserver(this)
    activity?.lifecycleScope?.launch {
      videoUriExtractor.fetchVideoFileFromPage(pageUrl).fold(
        ifLeft = { Timber.e(it, "Error fetching video url") },
        ifRight = { videoUrl -> player.playVideo(videoUrl) },
      )
    }
  }

  override fun
    onDetachedFromWindow() {
    super.onDetachedFromWindow()
    activity?.lifecycle?.removeObserver(this)
    player.onRelease()
  }

  private fun Context?.getActivity(): ComponentActivity? {
    var c = this
    while (c is ContextWrapper) {
      if (c is Activity) {
        return c as ComponentActivity?
      }
      c = c.baseContext
    }
    return null
  }
}
