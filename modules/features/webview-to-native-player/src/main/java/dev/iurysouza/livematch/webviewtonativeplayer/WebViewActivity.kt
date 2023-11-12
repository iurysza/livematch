package dev.iurysouza.livematch.webviewtonativeplayer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import dev.iurysouza.livematch.webviewtonativeplayer.detector.BackgroundVideoLoader
import dev.iurysouza.livematch.webviewtonativeplayer.player.VideoPlayer

@UnstableApi
class WebViewActivity : AppCompatActivity() {
  private lateinit var player: VideoPlayer

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_webview)
    val url = requireNotNull(intent.getStringExtra("uri")) { "The required URL is missing" }
    player = VideoPlayer(findViewById(R.id.player_view))
    val backgroundVideoLoader = BackgroundVideoLoader(findViewById(R.id.webview), player)
    backgroundVideoLoader.fetchVideoFrom(url)
  }

  override fun onPause() {
    super.onPause()
    player.onRelease()
  }

  companion object {
    fun start(context: Context, uri: String) {
      val intent = Intent(context, WebViewActivity::class.java)
      intent.putExtra("uri", uri)
      context.startActivity(intent)
    }
  }
}
