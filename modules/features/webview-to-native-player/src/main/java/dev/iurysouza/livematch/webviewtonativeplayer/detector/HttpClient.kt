package dev.iurysouza.livematch.webviewtonativeplayer.detector

internal interface HttpClient {
  suspend fun loadUrl(url: String): String?
}
