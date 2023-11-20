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

@SuppressLint("ViewConstructor", "UnsafeOptInUsageError")
class NativeVideoPlayerView(
  context: Context,
  private val pageUrl: String,
  private val eventListener: EventListener? = null,
  attrs: AttributeSet? = null,
) : FrameLayout(context, attrs), LifecycleEventObserver {

  private val activity by lazy { requireNotNull(context.getActivity()) }
  private val player by lazy {
    NativeVideoPlayer(
      activity.lifecycleScope,
      findViewById(R.id.player_view),
      findViewById(R.id.player_thumbnail),
      findViewById(R.id.player_play_button),
      eventListener,
    )
  }

  init {
    LayoutInflater.from(context).inflate(R.layout.native_video_player_view, this, true)
  }

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    when (event) {
      Lifecycle.Event.ON_PAUSE -> player.onPause()
      Lifecycle.Event.ON_RESUME -> player.onResume()
      else -> { /* no-op */
      }
    }
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    activity.lifecycle.addObserver(this)
    player.playVideo(pageUrl)
  }

  override fun
    onDetachedFromWindow() {
    super.onDetachedFromWindow()
    activity.lifecycle.removeObserver(this)
    player.onRelease()
  }

  fun interface EventListener {
    fun onEvent(event: NativePlayerEvent)
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


sealed interface NativePlayerEvent {
  object Playing : NativePlayerEvent
  object Ready : NativePlayerEvent

  sealed interface Error : NativePlayerEvent {
    object VideoScrapingFailed :
      Error

    object VideoPlaybackFailed : Error
    data class Unknown(val exception: Exception) :
      Error
  }
}
