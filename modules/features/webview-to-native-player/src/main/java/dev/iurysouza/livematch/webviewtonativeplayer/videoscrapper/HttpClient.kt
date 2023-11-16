package dev.iurysouza.livematch.webviewtonativeplayer.videoscrapper

internal interface HttpClient {
  suspend fun loadUrl(url: String): String?
}
