package dev.iurysouza.livematch.webviewtonativeplayer.player

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.view.ViewGroup
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.FullscreenButtonClickListener
import dev.iurysouza.livematch.webviewtonativeplayer.R

internal class FullScreenPlayer(
  private val playerView: PlayerView,
) : FullscreenButtonClickListener {
  private var isFullScreen = false
  private var parentView: ViewGroup? = playerView.parent as ViewGroup

  @Suppress("DEPRECATION")
  private val fullScreenDialog = object : Dialog(playerView.context, R.style.FullScreenDialogStyle) {
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
      if (isFullScreen) closeFullscreenDialog()
      else super.onBackPressed()
    }
  }

  init {
    playerView.setFullscreenButtonClickListener(this)
  }

  @SuppressLint("UnsafeOptInUsageError")
  override fun onFullscreenButtonClick(
    isFullScreen: Boolean,
  ) = if (isFullScreen) {
    playerView.hideController()
    openFullscreenDialog()
  } else {
    closeFullscreenDialog()
    playerView.hideController()
  }

  fun release() {
    fullScreenDialog.dismiss()
    parentView = null
  }

  @SuppressLint("UnsafeOptInUsageError")
  private fun openFullscreenDialog() {
    val height = Resources.getSystem().displayMetrics.heightPixels
    val width = height * 9 / 16

    val innerParams: ViewGroup.LayoutParams = playerView.layoutParams
    innerParams.height = height
    innerParams.width = width
    playerView.layoutParams = innerParams
    playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM


    val parent = playerView.parent as ViewGroup
    parent.removeView(playerView)

    val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    fullScreenDialog.addContentView(playerView, params)

    isFullScreen = true
    fullScreenDialog.show()
  }

  @SuppressLint("UnsafeOptInUsageError")
  private fun closeFullscreenDialog() {
    val width = Resources.getSystem().displayMetrics.widthPixels
    val height = width * 9 / 16
    playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
    val innerParams: ViewGroup.LayoutParams = playerView.layoutParams
    innerParams.height = height
    innerParams.width = width

    (playerView.parent as ViewGroup).removeView(playerView)

    parentView?.addView(playerView)
    isFullScreen = false
    fullScreenDialog.dismiss()
  }

}
