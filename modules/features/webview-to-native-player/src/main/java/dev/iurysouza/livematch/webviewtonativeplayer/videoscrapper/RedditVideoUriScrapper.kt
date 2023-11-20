package dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper

import arrow.core.Either
import arrow.core.flatMap
import org.json.JSONObject
import org.jsoup.Jsoup
import timber.log.Timber

internal class RedditVideoUriScrapper(
  private val httpClient: HttpClient = OkHttpCoroutineClient,
) : VideoUriExtractor {
  override suspend fun fetchVideoFileFromPage(url: String): Either<Exception, VideoInfo> =
    Either.catch { httpClient.loadUrl(url)!! }.mapLeft { e -> Exception("Failed to load url", e) }
      .flatMap { body -> extractVideoFromResponse(body) }
      .flatMap { validateMimeType(it) }
      .map { (videoUrl, mimeType) -> VideoInfo(videoUrl, mimeType, url) }
      .tap { Timber.v("Video detected: $it") }

  private fun validateMimeType(it: String?) = Either
    .catch { it!! to ScrapperHelper.getVideoMimeType(it)!! }
    .mapLeft { Exception("Invalid mime type", it) }

  private fun extractVideoFromResponse(body: String) = Either.catch {
    cleanUpUri(parseUriFromVideoFrame(body) ?: parseUriFromRedditPlayer(body))
  }.mapLeft { e -> Exception("Failed to extract video url from response body", e) }


  private fun parseUriFromRedditPlayer(html: String): String? {
    val redditPlayer = Jsoup.parse(html).selectFirst("shreddit-player")

    return try {
      val permutations = JSONObject(redditPlayer!!.attr("packaged-media-json"))
        .getJSONObject("playbackMp4s")
        .getJSONArray("permutations")

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
      redditPlayer!!.attr("preview").replace("DASH_96", "DASH_480")
    }
  }

  private fun parseUriFromVideoFrame(
    html: String,
  ): String? = Jsoup.parse(html).select("video").first()?.attr("src")

  /**
   * Usually happens with links like https://streamable.com/ur6s67
   * Where the video src is starts like: //cdn.streamable.com/video/mp4/ur6s67.mp4
   */
  private fun cleanUpUri(
    uri: String?,
  ): String? = if (uri == null || !uri.startsWith("//")) {
    uri
  } else {
    uri.replace("//", "https://")
  }
}

internal interface VideoUriExtractor {
  suspend fun fetchVideoFileFromPage(url: String): Either<Exception, VideoInfo>
}
