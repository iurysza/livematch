package dev.iurysouza.livematch.webviewtonativeplayer.player

import android.app.Dialog
import android.view.ViewGroup
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.FullscreenButtonClickListener
import com.google.android.material.R.style
import dev.iurysouza.livematch.webviewtonativeplayer.R

class FullScreenPlayer(
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
    val parent = playerView.parent as ViewGroup
    parent.removeView(playerView)

    val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    fullScreenDialog.addContentView(playerView, params)

    isFullScreen = true
    fullScreenDialog.show()
  }

  private fun closeFullscreenDialog() {
    (playerView.parent as ViewGroup).removeView(playerView)

    parentView?.addView(playerView)
    isFullScreen = false
    fullScreenDialog.dismiss()
  }

}
