package dev.iurysouza.livematch

import android.app.Application
import android.content.Context
import coil.Coil
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LiveMatchApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    Coil.setImageLoader(LiveMatchImageLoader(this))
  }

  private class LiveMatchImageLoader(private val context: Context) : ImageLoaderFactory {
    override fun newImageLoader() = ImageLoader.Builder(context)
      .components { add(SvgDecoder.Factory()) }
      .build()
  }
}
