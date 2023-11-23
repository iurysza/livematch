package dev.iurysouza.livematch.webviewtonativeplayer.player

import android.widget.ImageView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import coil.load
import com.fresh.materiallinkpreview.parsing.OpenGraphMetaDataProvider
import dev.iurysouza.livematch.webviewtonativeplayer.NativePlayerEvent
import dev.iurysouza.livematch.webviewtonativeplayer.NativeVideoPlayerView
import dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper.RedditVideoUriScrapper
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@UnstableApi
internal class NativeVideoPlayer(
  private val lifecycleCoroutineScope: CoroutineScope?,
  private val localPlayerView: PlayerView,
  private val thumbnail: ImageView,
  private val playButton: ImageView,
  private val listener: NativeVideoPlayerView.EventListener?,
) : NativeVideoPlayerView.EventListener {

  private var playerManager: PlayerManager? = null
  private val metaDataProvider by lazy { OpenGraphMetaDataProvider() }
  private val videoUriExtractor by lazy { RedditVideoUriScrapper() }

  init {
    localPlayerView.requestFocus()
    onInit()
    playButton.setOnClickListener {
      thumbnail.visibility = ImageView.GONE
      playButton.visibility = ImageView.GONE
      playerManager?.forcePlay()
    }
  }

  private fun onInit() {
    if (playerManager == null || playerManager?.getPlaybackMode() === PlayerManager.PlaybackMode.RELEASED) {
      val fullScreenManager = FullScreenPlayer(localPlayerView);
      playerManager = PlayerManager(localPlayerView, fullScreenManager)
      listener?.let { playerManager?.addListener(it) }
      playerManager?.addListener(this)
    }
  }

  fun onRelease() {
    playerManager?.setPlaybackMode(PlayerManager.PlaybackMode.RELEASED)
    playerManager = null
  }


  fun onPause() {
    Timber.v("OnPause")
    playerManager?.pause()
  }

  fun onResume() {
    Timber.v("OnResume")
    playerManager?.play()
  }

  fun playVideo(pageUrl: String) {
    lifecycleCoroutineScope?.launch {
      handleVideoThumbnail(pageUrl)
      videoUriExtractor.fetchVideoDataFromPage(pageUrl).fold(
        ifLeft = { listener?.onEvent(NativePlayerEvent.Error.VideoScrapingFailed) },
        ifRight = { videoData -> playerManager?.addItem(videoData) },
      )
    }
  }

  private suspend fun handleVideoThumbnail(url: String) {
    metaDataProvider.startFetchingMetadataAsync(URL(url))
      .onFailure {
        Timber.e(it, "Failed to fetch metadata")
      }
      .onSuccess {
        Timber.v("Url thumbnail fetched: ${it.imageUrl}")
        thumbnail.load(it.imageUrl) { crossfade(true) }
      }
  }

  override fun onEvent(event: NativePlayerEvent) {
    Timber.v("OnEvent: $event")
    playButton.showIf(event !is NativePlayerEvent.Playing)
  }
}
