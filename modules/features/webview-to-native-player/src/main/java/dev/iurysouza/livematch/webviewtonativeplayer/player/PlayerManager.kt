package dev.iurysouza.livematch.webviewtonativeplayer.player

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.DiscontinuityReason
import androidx.media3.common.Player.PlayWhenReadyChangeReason
import androidx.media3.common.Player.TimelineChangeReason
import androidx.media3.common.Timeline
import androidx.media3.common.util.Clock
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.DefaultAnalyticsCollector
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.ui.PlayerView
import dev.iurysouza.livematch.webviewtonativeplayer.detector.SharedUtils
import dev.iurysouza.livematch.webviewtonativeplayer.detector.VideoInfo
import timber.log.Timber

@UnstableApi
/** Manages players and an internal media queue  */
internal class PlayerManager(
  private var queuePositionListener: QueuePositionListener?,
  private var playerView: PlayerView?,
  private var fullScreenManager: FullScreenPlayer?,
) : Player.Listener {
  /**
   * Listener for changes in the media queue playback position.
   */
  interface QueuePositionListener {
    /**
     * Called when the currently played item of the media queue changes.
     */
    fun onQueuePositionChanged(previousIndex: Int, newIndex: Int)
  }

  private var mediaQueue: ArrayList<VideoInfo>?
  private var concatenatingMediaSource: ConcatenatingMediaSource?
  private var exoPlayer: ExoPlayer?
  private var dataSourceFactory: DefaultHttpDataSource.Factory?

  /**
   * Returns the index of the currently played item.
   */
  private var currentItemIndex: Int = C.INDEX_UNSET
  private var currentPlayer: Player? = null

  enum class PlaybackMode {
    NORMAL, RELEASED
  }

  private var playbackMode: PlaybackMode

  init {
    mediaQueue = ArrayList()
    concatenatingMediaSource = ConcatenatingMediaSource()
    exoPlayer = buildExoPlayer(playerView!!.context)
    playerView?.player = exoPlayer
    dataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent(SharedUtils.userAgent)
    currentItemIndex = C.INDEX_UNSET
    playbackMode = PlaybackMode.NORMAL
    setCurrentPlayer(exoPlayer!!)
  }

  private fun buildExoPlayer(context: Context) = ExoPlayer.Builder(context)
    .setTrackSelector(DefaultTrackSelector(playerView!!.context))
    .setAnalyticsCollector(DefaultAnalyticsCollector(Clock.DEFAULT).apply { addListener(EventLogger()) })
    .build().also { it.addListener(this) }

  // ===========================================================================
  // Queue manipulation methods.
  // ===========================================================================
  /**
   * Plays a specified queue item in the current player.
   *
   * @param itemIndex The index of the item to play.
   */
  fun selectQueueItem(itemIndex: Int) {
    setCurrentItem(itemIndex, C.TIME_UNSET, true)
  }

  /**
   * Appends `sample` to the media queue.
   *
   * @param video The [VideoInfo] to append.
   */
  fun addItem(video: VideoInfo) {
    mediaQueue?.add(video)
    concatenatingMediaSource?.addMediaSource(buildMediaSource(video))
  }

  val mediaQueueSize: Int
    /**
     * Returns the size of the media queue.
     */
    get() = mediaQueue!!.size

  /**
   * Returns the item at the given index in the media queue.
   *
   * @param position The index of the item.
   * @return The item at the given index in the media queue.
   */
  fun getItem(position: Int): VideoInfo? = if (position in 0 until mediaQueueSize) {
    mediaQueue!![position]
  } else {
    null
  }

  /**
   * Removes the item at the given index from the media queue.
   *
   * @param itemIndex The index of the item to remove.
   * @return Whether the removal was successful.
   */
  fun removeItem(itemIndex: Int): Boolean {
    concatenatingMediaSource!!.removeMediaSource(itemIndex)
    mediaQueue!!.removeAt(itemIndex)
    if (itemIndex == currentItemIndex && itemIndex == mediaQueue!!.size) {
      maybeSetCurrentItemAndNotify(C.INDEX_UNSET)
    } else if (itemIndex < currentItemIndex) {
      maybeSetCurrentItemAndNotify(currentItemIndex - 1)
    }
    return true
  }

  /**
   * Moves an item within the queue.
   *
   * @param fromIndex The index of the item to move.
   * @param toIndex The target index of the item in the queue.
   * @return Whether the item move was successful.
   */
  fun moveItem(fromIndex: Int, toIndex: Int): Boolean {
    // Player update.
    concatenatingMediaSource!!.moveMediaSource(fromIndex, toIndex)
    mediaQueue!!.add(toIndex, mediaQueue!!.removeAt(fromIndex))

    // Index update.
    when {
      fromIndex == currentItemIndex -> maybeSetCurrentItemAndNotify(toIndex)
      currentItemIndex in (fromIndex + 1)..toIndex -> maybeSetCurrentItemAndNotify(currentItemIndex - 1)
      currentItemIndex in toIndex until fromIndex -> maybeSetCurrentItemAndNotify(currentItemIndex + 1)
    }
    return true
  }


  fun getPlaybackMode(): PlaybackMode {
    return playbackMode
  }

  fun setPlaybackMode(playbackMode: PlaybackMode?) {
    when (playbackMode) {
      PlaybackMode.NORMAL -> {}
      PlaybackMode.RELEASED -> release()
      else -> return
    }
    this.playbackMode = playbackMode
  }

  /**
   * Releases the manager and the players that it holds.
   */
  private fun release() {
    if (playbackMode == PlaybackMode.RELEASED) return
    try {
      releaseExoplayer()
      fullScreenManager?.release()
      mediaQueue!!.clear()
      concatenatingMediaSource!!.clear()
      queuePositionListener = null
      mediaQueue = null
      concatenatingMediaSource = null
      dataSourceFactory = null
      currentItemIndex = C.INDEX_UNSET
      currentPlayer = null
      fullScreenManager = null
    } catch (e: Exception) {
      Timber.e(e, "Error releasing player manager")
    }
    playbackMode = PlaybackMode.RELEASED
  }

  private fun releaseExoplayer() {
    if (exoPlayer == null) return
    try {
      playerView!!.player = null
      fullScreenManager!!.release()
      exoPlayer!!.removeListener(this)
      exoPlayer!!.release()
      playerView = null
      fullScreenManager = null
      exoPlayer = null
    } catch (e: Exception) {
      Timber.e(e, "Error releasing exoPlayer")
    }
  }


  // ===========================================================================
  // https://github.com/androidx/media/blob/1.0.0-beta03/libraries/common/src/main/java/androidx/media3/common/Player.java#L625-L1073
  // ===========================================================================
  // Player.Listener implementation.
  // ===========================================================================
  override fun onPlaybackStateChanged(playbackState: @Player.State Int) {
    updateCurrentItemIndex()
  }

  override fun onPlayWhenReadyChanged(
    playWhenReady: Boolean,
    reason: @PlayWhenReadyChangeReason Int,
  ) {
    updateCurrentItemIndex()
  }

  override fun onPositionDiscontinuity(
    oldPosition: Player.PositionInfo,
    newPosition: Player.PositionInfo,
    reason: @DiscontinuityReason Int,
  ) {
    updateCurrentItemIndex()
  }

  override fun onTimelineChanged(timeline: Timeline, reason: @TimelineChangeReason Int) {
    updateCurrentItemIndex()
  }

  override fun onPlayerError(error: PlaybackException) {
    Timber.e("PlayerManager", "onPlayerError: $error", error.cause)
  }

  private fun updateCurrentItemIndex() {
    if (currentPlayer == null) return
    val playbackState = currentPlayer!!.playbackState
    maybeSetCurrentItemAndNotify(
      if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
        currentPlayer!!.currentMediaItemIndex
      } else {
        C.INDEX_UNSET
      },
    )
  }

  private fun setCurrentPlayer(currentPlayer: Player) {
    if (this.currentPlayer === currentPlayer) {
      return
    }

    (playerView!!.parent as ViewGroup).visibility = View.VISIBLE

    // Player state management.
    var playbackPositionMs = C.TIME_UNSET
    var windowIndex = C.INDEX_UNSET
    if (this.currentPlayer != null) {
      val playbackState = this.currentPlayer!!.playbackState
      if (playbackState != Player.STATE_ENDED) {
        playbackPositionMs = this.currentPlayer!!.currentPosition
        windowIndex = this.currentPlayer!!.currentMediaItemIndex
        if (windowIndex != currentItemIndex) {
          playbackPositionMs = C.TIME_UNSET
          windowIndex = currentItemIndex
        }
      }
    } else {
      // This is the initial setup. No need to save any state.
    }
    currentPlayer.playWhenReady = true

    // Media queue management.
    exoPlayer?.setMediaSource(concatenatingMediaSource!!)
    exoPlayer?.prepare()
    this.currentPlayer = currentPlayer

    // Playback transition.
    if (windowIndex != C.INDEX_UNSET) {
      setCurrentItem(windowIndex, playbackPositionMs, true)
    }
  }

  /**
   * Starts playback of the item at the given position.
   *
   * @param itemIndex The index of the item to play.
   * @param positionMs The position at which playback should start.
   * @param playWhenReady Whether the player should proceed when ready to do so.
   */
  private fun setCurrentItem(itemIndex: Int, positionMs: Long, playWhenReady: Boolean) {
    maybeSetCurrentItemAndNotify(itemIndex)
    currentPlayer!!.seekTo(itemIndex, positionMs)
    currentPlayer!!.playWhenReady = playWhenReady
  }

  private fun maybeSetCurrentItemAndNotify(currentItemIndex: Int) {
    if (this.currentItemIndex != currentItemIndex) {
      val oldIndex = this.currentItemIndex
      this.currentItemIndex = currentItemIndex
      queuePositionListener!!.onQueuePositionChanged(oldIndex, currentItemIndex)
      if (currentItemIndex != C.INDEX_UNSET) {
        setHttpRequestHeaders(currentItemIndex)
      }
    }
  }

  private fun setHttpRequestHeaders(currentItemIndex: Int) {
    if (dataSourceFactory == null) return
    val sample = getItem(currentItemIndex) ?: return
    dataSourceFactory!!.setDefaultRequestProperties(sample.reqHeadersMap)
  }

  private fun buildMediaSource(video: VideoInfo): MediaSource {
    val dsFactory = requireNotNull(dataSourceFactory) { "dataSourceFactory must not be null" }
    val mediaItem = with(video) {
      MediaItem
        .Builder()
        .setUri(uri).apply {
          if (mimeType.isNotBlank()) {
            setMimeType(mimeType)
          }
        }.build()
    }

    return when (video.mimeType) {
      MimeTypes.APPLICATION_M3U8 -> HlsMediaSource.Factory(dsFactory).createMediaSource(mediaItem)
      MimeTypes.APPLICATION_MPD -> DashMediaSource.Factory(dsFactory).createMediaSource(mediaItem)
      MimeTypes.APPLICATION_SS -> SsMediaSource.Factory(dsFactory).createMediaSource(mediaItem)
      else -> ProgressiveMediaSource.Factory(dsFactory).createMediaSource(mediaItem)
    }
  }

  fun pause() {
    currentPlayer?.pause()
    Timber.v("Pausing player")
  }

  fun play() {
    Timber.v("Playing content")
    currentPlayer?.play()
  }
}
