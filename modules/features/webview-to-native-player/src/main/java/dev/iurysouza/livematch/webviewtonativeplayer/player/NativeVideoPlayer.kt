package dev.iurysouza.livematch.webviewtonativeplayer.player

import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper.VideoInfo

@UnstableApi
internal class NativeVideoPlayer(
  private val localPlayerView: PlayerView,
) : PlayerManager.QueuePositionListener {

  private var playerManager: PlayerManager? = null

  init {
    localPlayerView.requestFocus()
    onInit()
  }

  private fun onInit() {
    if (playerManager == null || playerManager?.getPlaybackMode() === PlayerManager.PlaybackMode.RELEASED) {
      val fullScreenManager = FullScreenPlayer(localPlayerView);
      playerManager = PlayerManager(this, localPlayerView, fullScreenManager)
    }
  }

  fun onRelease() {
    playerManager?.setPlaybackMode(PlayerManager.PlaybackMode.RELEASED)
    playerManager = null;
  }

  override fun onQueuePositionChanged(previousIndex: Int, newIndex: Int) {
  }

  fun playVideo(videoInfo: VideoInfo) {
    playerManager?.addItem(videoInfo)
  }


  fun pause() {
    playerManager?.pause()
  }

  fun play() {
    playerManager?.play()
  }
}
