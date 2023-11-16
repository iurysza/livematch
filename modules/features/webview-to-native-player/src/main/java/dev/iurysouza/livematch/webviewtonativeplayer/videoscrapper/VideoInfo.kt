package dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper

import android.net.Uri

internal data class VideoInfo(
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

  override fun toString() = "VideoInfo(uri='$uri', mimeType='$mimeType', referer='$referer)\n"
}
