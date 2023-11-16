package dev.iurysouza.livematch.webviewtonativeplayer.detector

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled")
class WebViewBackgroundVideoLoader(
  private val webView: WebView,
) : BackgroundVideoLoader {
  private var videoDetectorListener: VideoDetectionListener? = null
  override fun setVideoDetectorListener(listener: VideoDetectionListener) {
    videoDetectorListener = listener
  }

  override suspend fun fetchVideoFileFromPage(url: String): VideoInfo? {
    TODO("Not yet implemented")
  }

  private val client: VideoDetectorWebViewClient by lazy {
    VideoDetectorWebViewClient(
      onVideoDetectionFinished = { videoSet ->
        Timber.d("VideoDetectionFinished: $videoSet")
        videoDetectorListener?.onVideoDetected(videoSet)
      },
    )
  }

  init {
    webView.clearCache(true)
    webView.clearHistory()
    webView.webViewClient = client
    webView.setDownloadListener(client)
    webView.settings.apply {
      loadWithOverviewMode = true
      javaScriptEnabled = true
      domStorageEnabled = true
      userAgentString = SharedUtils.userAgent
      mediaPlaybackRequiresUserGesture = false
      mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }
  }

  override fun fetchVideoFrom(url: String) {
    webView.loadUrl(url)
  }


}

interface VideoDetectionListener {
  fun onVideoDetected(videos: Set<VideoInfo>)
}

interface BackgroundVideoLoader {
  fun setVideoDetectorListener(listener: VideoDetectionListener)
  suspend fun fetchVideoFileFromPage(url: String): VideoInfo?
  fun fetchVideoFrom(url: String)

}
