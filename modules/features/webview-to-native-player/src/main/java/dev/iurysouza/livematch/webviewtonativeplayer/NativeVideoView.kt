package dev.iurysouza.livematch.webviewtonativeplayer

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import dev.iurysouza.livematch.webviewtonativeplayer.detector.BackgroundVideoLoader
import dev.iurysouza.livematch.webviewtonativeplayer.player.VideoPlayer

@SuppressLint("ViewConstructor", "UnsafeOptInUsageError")
class NativeVideoView(
  initUrl: String,
  context: Context,
  attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

  private var player: VideoPlayer
  private val webView: WebView
  private val videoView: PlayerView
  private var backgroundVideoLoader: BackgroundVideoLoader

  init {
    LayoutInflater.from(context).inflate(R.layout.activity_webview, this, true)
    webView = findViewById(R.id.webview)
    videoView = findViewById(R.id.player_view)
    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    player = VideoPlayer(videoView)
    backgroundVideoLoader = BackgroundVideoLoader(webView, player)
    loadUrl(initUrl)
  }

  @UnstableApi
  override fun
    onDetachedFromWindow() {
    super.onDetachedFromWindow()
    player.onRelease()
  }

  fun loadUrl(uri: String) {
    backgroundVideoLoader.fetchVideoFrom(uri)
  }
}
