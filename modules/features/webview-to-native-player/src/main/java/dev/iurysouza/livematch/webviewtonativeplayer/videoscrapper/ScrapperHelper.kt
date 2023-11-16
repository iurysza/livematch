package dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper

import java.util.Locale
import timber.log.Timber

internal object ScrapperHelper {
  val userAgent =
    """Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36"""
  private val videoRegex =
    """\.(mp4|mp4v|mpv|m1v|m4v|mpg|mpg2|mpeg|xvid|webm|3gp|avi|mov|mkv|ogg|ogv|ogm|m3u8|mpd|ism(?:[vc]|/manifest)?)(?:[?#]|$)""".toRegex(
      RegexOption.IGNORE_CASE,
    )

  private val fallbackVideoRegex =
    """filename=.*\.(mp4|mp4v|mpv|m1v|m4v|mpg|mpg2|mpeg|xvid|webm|3gp|avi|mov|mkv|ogg|ogv|ogm|m3u8|mpd|ism(?:[vc]|/manifest)?)(?:&|$)""".toRegex(
      RegexOption.IGNORE_CASE,
    )

  fun getVideoMimeType(uri: String?): String? {
    if (uri == null) return null

    val matchResult = videoRegex.find(uri) ?: fallbackVideoRegex.find(uri)
    val fileExtension = matchResult?.groupValues?.get(1) ?: return null

    return when (fileExtension.lowercase(Locale.getDefault())) {
      "mp4", "mp4v", "m4v" -> "video/mp4"
      "mpv" -> "video/MPV"
      "m1v", "mpg", "mpg2", "mpeg" -> "video/mpeg"
      "xvid" -> "video/x-xvid"
      "webm" -> "video/webm"
      "3gp" -> "video/3gpp"
      "avi" -> "video/x-msvideo"
      "mov" -> "video/quicktime"
      "mkv" -> "video/x-mkv"
      "ogg", "ogv", "ogm" -> "video/ogg"
      "m3u8" -> "application/x-mpegURL"
      "mpd" -> "application/dash+xml"
      "ism", "ism/manifest", "ismv", "ismc" -> "application/vnd.ms-sstr+xml"
      else -> {
        Timber.d("Unknown video mime type for $fileExtension")
        null
      }
    }
  }
}
