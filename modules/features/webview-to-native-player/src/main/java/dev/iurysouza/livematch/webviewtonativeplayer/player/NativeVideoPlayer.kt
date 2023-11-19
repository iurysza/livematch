package dev.iurysouza.livematch.webviewtonativeplayer.player

import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import dev.iurysouza.livematch.webviewtonativeplayer.NativePlayerEvent
import dev.iurysouza.livematch.webviewtonativeplayer.NativeVideoPlayerView
import dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper.RedditVideoUriScrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@UnstableApi
internal class NativeVideoPlayer(
  private val lifecycleCoroutineScope: CoroutineScope,
  private val localPlayerView: PlayerView,
  private val listener: NativeVideoPlayerView.EventListener?,
) {

  private var playerManager: PlayerManager? = null

  private val videoUriExtractor by lazy { RedditVideoUriScrapper() }

  init {
    localPlayerView.requestFocus()
    onInit()
  }

  private fun onInit() {
    if (playerManager == null || playerManager?.getPlaybackMode() === PlayerManager.PlaybackMode.RELEASED) {
      val fullScreenManager = FullScreenPlayer(localPlayerView);
      playerManager = PlayerManager(localPlayerView, fullScreenManager, listener)
    }
  }

  fun onRelease() {
    playerManager?.setPlaybackMode(PlayerManager.PlaybackMode.RELEASED)
    playerManager = null;
  }


  fun onPause() {
    Timber.v("OnPause")
    playerManager?.pause()
  }

  fun onResume() {
    Timber.v("OnResume")
    playerManager?.play()
  }

  fun playVideo(videoInfo: String) {
    lifecycleCoroutineScope.launch {
      videoUriExtractor.fetchVideoFileFromPage(videoInfo).fold(
        ifLeft = { listener?.onEvent(NativePlayerEvent.Error.VideoScrapingFailed) },
        ifRight = { videoInfo -> playerManager?.addItem(videoInfo) },
      )
    }
  }
}
