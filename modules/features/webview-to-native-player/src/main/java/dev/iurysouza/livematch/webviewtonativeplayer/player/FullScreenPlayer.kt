package dev.iurysouza.livematch.webviewtonativeplayer.player

import android.app.Dialog
import android.content.res.Resources
import android.view.ViewGroup
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

  override fun onFullscreenButtonClick(
    isFullScreen: Boolean,
  ) = if (isFullScreen) {
    openFullscreenDialog()
  } else {
    closeFullscreenDialog()
  }

  fun release() {
    fullScreenDialog.dismiss()
    parentView = null
  }

  private fun openFullscreenDialog() {
    val height = Resources.getSystem().displayMetrics.heightPixels
    val width = height * 9 / 16

    val innerParams: ViewGroup.LayoutParams = playerView.layoutParams
    innerParams.height = height
    innerParams.width = width
    playerView.layoutParams = innerParams


    val parent = playerView.parent as ViewGroup
    parent.removeView(playerView)

    val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    fullScreenDialog.addContentView(playerView, params)

    isFullScreen = true
    fullScreenDialog.show()
  }

  private fun closeFullscreenDialog() {
    val width = Resources.getSystem().displayMetrics.widthPixels
    val height = width * 9 / 16

    val innerParams: ViewGroup.LayoutParams = playerView.layoutParams
    innerParams.height = height
    innerParams.width = width

    (playerView.parent as ViewGroup).removeView(playerView)

    parentView?.addView(playerView)
    isFullScreen = false
    fullScreenDialog.dismiss()
  }

}
