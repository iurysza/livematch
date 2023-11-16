package dev.iurysouza.livematch.webviewtonativeplayer.detector

import org.json.JSONObject
import org.jsoup.Jsoup
import timber.log.Timber

class OkHTTPVideoLoader : BackgroundVideoLoader {
  override fun setVideoDetectorListener(listener: VideoDetectionListener) {}

  override suspend fun fetchVideoFileFromPage(url: String): VideoInfo? {
    val body = HttpClient.loadUrl(url) ?: return null

    val videoUrl = runCatching {
      cleanUpUri(uri = parseUriFromVideoFrame(body) ?: parseUriFromRedditPlayer(body))
    }.onFailure { e ->
      Timber.e(e, "Failed to extract video url from $url")
    }.getOrNull() ?: return null

    val mimeType = SharedUtils.getVideoMimeType(videoUrl) ?: return null
    val videoInfo = VideoInfo(
      uri = videoUrl,
      mimeType = mimeType,
      referer = url,
    )
    Timber.v("Video detected: $videoInfo")
    return videoInfo
  }

  private fun parseUriFromRedditPlayer(html: String): String? {
    val redditPlayer = Jsoup.parse(html).selectFirst("shreddit-player")

    return try {
      // Attempt to parse packaged-media-json
      val packagedMediaJson = redditPlayer.attr("packaged-media-json")
      val json = JSONObject(packagedMediaJson)
      val playbackMp4s = json.getJSONObject("playbackMp4s")
      val permutations = playbackMp4s.getJSONArray("permutations")

      var selectedUrl: String? = null
      var maxHeight = 0

      for (i in 0 until permutations.length()) {
        val source = permutations.getJSONObject(i).getJSONObject("source")
        val url = source.getString("url")
        val dimensions = source.getJSONObject("dimensions")
        val height = dimensions.getInt("height")

        if (height > maxHeight) {
          selectedUrl = url
          maxHeight = height
        }
      }
      selectedUrl
    } catch (e: Exception) {
      Timber.e(e, "Error parsing packagedMediaJson, falling back to preview attribute.")
      redditPlayer.attr("preview").replace("DASH_96", "DASH_480")
    }
  }

  private fun parseUriFromVideoFrame(html: String) = Jsoup.parse(html).select("video").first()?.attr("src")

  /**
   * Usually happens with links like https://streamable.com/ur6s67
   * Where the video src is starts like: //cdn.streamable.com/video/mp4/ur6s67.mp4
   */
  private fun cleanUpUri(uri: String?): String? {
    if (uri == null || !uri.startsWith("//")) return uri
    return uri.replace("//", "https://")
  }

  override fun fetchVideoFrom(url: String) {}

}


