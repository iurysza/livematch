package dev.iurysouza.livematch.webviewtonativeplayer.detector

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Handler
import android.webkit.DownloadListener
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.media3.common.MediaItem

class VideoDetectorWebViewClient(
  val onVideoDetected: (VideoInfo) -> Unit,
) : DownloadListener, WebViewClient() {

  override fun onLoadResource(view: WebView?, url: String) {
    processUrl(url, view)
  }

  @Deprecated("Deprecated in Java")
  override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
    processUrl(url, view)
    return false
  }

  override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
    val url: String = request.url.toString()
    processUrl(url, view)
    return false
  }

  @Deprecated("Deprecated in Java")
  override fun shouldInterceptRequest(view: WebView?, url: String): WebResourceResponse? {
    processUrl(url, view)
    return null
  }

  override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse? {
    val url: String = request.url.toString()
    processUrl(url, view)
    return null
  }

  @SuppressLint("NewApi")
  private fun processUrl(uri: String, view: WebView?) {
    val looper = view?.webViewLooper ?: return
    val mimeType = SharedUtils.getVideoMimeType(uri) ?: return
    Handler(looper).post { onVideoDetected(VideoInfo(uri, mimeType, view.url)) }
  }

  override fun onDownloadStart(
    url: String?,
    userAgent: String?,
    contentDisposition: String?,
    mimetype: String?,
    contentLength: Long,
  ) {
    url?.let { processUrl(it, null) }
  }
}


data class VideoInfo(
  val uri: String,
  val mimeType: String,
  val referer: String?,
) {
  val reqHeadersMap: HashMap<String, String> = hashMapOf()

  init {
    // Setting referer and origin headers if referer is not empty
    if (!referer.isNullOrBlank()) {
      reqHeadersMap["referer"] = referer

      val refererUri = Uri.parse(referer)
      val origin = "${refererUri.scheme}://${refererUri.authority}"
      reqHeadersMap["origin"] = origin
    }
  }

  override fun toString() = uri

  fun asMediaItem() = MediaItem
    .Builder()
    .setUri(uri).apply {
      if (mimeType.isNotBlank()) {
        setMimeType(mimeType)
      }
    }.build()
}
