package dev.iurysouza.livematch.webviewtonativeplayer.detector

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import timber.log.Timber

@SuppressLint("SetJavaScriptEnabled")
class BackgroundVideoLoader(
  private val webView: WebView,
  private val videoDetectorListener: VideoDetectorListener,
) {

  private var videoInfoSet = setOf<VideoInfo>()
  private val client: VideoDetectorWebViewClient by lazy {
    VideoDetectorWebViewClient(
      onVideoDetected = { video ->
        videoInfoSet += video
        Timber.e("videoInfoSet: $videoInfoSet")
        videoDetectorListener.onVideoDetected(video)
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

  fun fetchVideoFrom(url: String) {
    webView.loadUrl(url)
  }

}

interface VideoDetectorListener {
  fun onVideoDetected(video: VideoInfo)
}
