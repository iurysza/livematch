package dev.iurysouza.livematch.webviewtonativeplayer.player

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.DiscontinuityReason
import androidx.media3.common.Player.PlayWhenReadyChangeReason
import androidx.media3.common.Player.TimelineChangeReason
import androidx.media3.common.Timeline
import androidx.media3.common.util.Clock
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
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
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING
import dev.iurysouza.livematch.webviewtonativeplayer.NativePlayerEvent
import dev.iurysouza.livematch.webviewtonativeplayer.NativeVideoPlayerView
import dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper.ScrapperHelper
import dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper.VideoData
import timber.log.Timber
import androidx.media3.ui.R as Media3R

@UnstableApi
/** Manages players and an internal media queue  */
internal class PlayerManager(
  private var playerView: PlayerView?,
  private var fullScreenManager: FullScreenPlayer?,
) : Player.Listener {
  /**
   * Listener for changes in the media queue playback position.
   */


  private var listeners = mutableListOf<NativeVideoPlayerView.EventListener>()
  private var mediaQueue: ArrayList<VideoData>?
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
  override fun onAvailableCommandsChanged(availableCommands: Player.Commands) {
    super.onAvailableCommandsChanged(availableCommands)
    if (availableCommands.contains(Player.COMMAND_PREPARE)) {
      listeners.forEach { it.onEvent(NativePlayerEvent.Ready) }
      Timber.v("Video prepared")
      playerView?.showController()
    }
  }

  init {
    mediaQueue = ArrayList()
    concatenatingMediaSource = ConcatenatingMediaSource()
    exoPlayer = buildExoPlayer(playerView!!.context)
    playerView?.player = exoPlayer
    dataSourceFactory = DefaultHttpDataSource.Factory().setUserAgent(ScrapperHelper.userAgent)
    currentItemIndex = C.INDEX_UNSET
    playbackMode = PlaybackMode.NORMAL
    setCurrentPlayer(exoPlayer!!)
    exoPlayer?.addListener(this)
    setupCustomControllerBehavior()
  }

  private fun setupCustomControllerBehavior() = playerView?.apply {
    setRepeatToggleModes(Player.REPEAT_MODE_ALL)
    setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING)
    findViewById<ImageButton>(Media3R.id.exo_playback_speed)?.setOnClickListener {
      exoPlayer?.togglePlaybackSpeed(it)
      hideController()
    }
  }


  private fun setSpinnerColor() {
    val color = "#F8E94D"
    val bar = playerView?.findViewById<ProgressBar>(Media3R.id.exo_buffering)
    bar?.indeterminateDrawable?.setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY)
  }


  private fun ExoPlayer.togglePlaybackSpeed(imageButton: View) {
    if (imageButton !is ImageButton) return
    val speed = if (playbackParameters.speed == 1f) {
      imageButton.setImageDrawable(AppCompatResources.getDrawable(imageButton.context, Media3R.drawable.exo_icon_play))
      0.5f
    } else {
      imageButton.setImageDrawable(AppCompatResources.getDrawable(imageButton.context, Media3R.drawable.exo_ic_speed))
      1f
    }
    playbackParameters = PlaybackParameters(speed)
  }

  private fun buildExoPlayer(context: Context) = ExoPlayer.Builder(context)
    .setTrackSelector(DefaultTrackSelector(playerView!!.context))
    .setAnalyticsCollector(DefaultAnalyticsCollector(Clock.DEFAULT).apply { addListener(EventLogger()) })
    .build().also { it.addListener(this) }

  /**
   * Appends `sample` to the media queue.
   *
   * @param video The [VideoData] to append.
   */
  fun addItem(video: VideoData) {
    mediaQueue?.add(video)
    concatenatingMediaSource?.addMediaSource(buildMediaSource(video))
    setSpinnerColor()
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
  fun getItem(position: Int): VideoData? = if (position in 0 until mediaQueueSize) {
    mediaQueue!![position]
  } else {
    null
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
    listeners.forEach { it.onEvent(NativePlayerEvent.Error.Unknown(error)) }
    Timber.e(error, "Something went wrong with the player")
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
    exoPlayer?.addListener(this)

    // Playback transition.
    if (windowIndex != C.INDEX_UNSET) {
      setCurrentItem(windowIndex, playbackPositionMs)
    }
  }

  private fun setCurrentItem(itemIndex: Int, positionMs: Long) {
    maybeSetCurrentItemAndNotify(itemIndex)
    currentPlayer!!.seekTo(itemIndex, positionMs)
    currentPlayer!!.playWhenReady = true
  }

  private fun maybeSetCurrentItemAndNotify(currentItemIndex: Int) {
    if (this.currentItemIndex != currentItemIndex) {
      this.currentItemIndex = currentItemIndex
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

  private fun buildMediaSource(video: VideoData): MediaSource {
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
  }

  fun play() {
    currentPlayer?.play()
  }

  fun forcePlay() {
    Util.handlePlayButtonAction(currentPlayer)
    playerView?.hideController()
    Timber.v("Playback started")
    listeners.forEach { it.onEvent(NativePlayerEvent.Playing) }
  }

  fun addListener(listener: NativeVideoPlayerView.EventListener) {
    listeners.add(listener)
  }
}
