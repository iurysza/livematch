package dev.iurysouza.livematch.webviewtonativeplayer.player

import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import dev.iurysouza.livematch.webviewtonativeplayer.detector.VideoDetectorListener
import dev.iurysouza.livematch.webviewtonativeplayer.detector.VideoInfo

@UnstableApi
class VideoPlayer(
  private val localPlayerView: PlayerView,
) : PlayerManager.QueuePositionListener, VideoDetectorListener {

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

  override fun onVideoDetected(video: VideoInfo) {
    playerManager?.addItem(video)
  }
}
