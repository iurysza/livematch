package dev.iurysouza.livematch

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import dagger.hilt.android.HiltAndroidApp
import dev.iurysouza.livematch.common.LiveMatchDebugTree
import timber.log.Timber

@HiltAndroidApp
class LiveMatchApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(LiveMatchDebugTree)
    Coil.setImageLoader {
      ImageLoader.Builder(this@LiveMatchApp)
        .components { add(SvgDecoder.Factory()) }
        .build()
    }
  }
}
