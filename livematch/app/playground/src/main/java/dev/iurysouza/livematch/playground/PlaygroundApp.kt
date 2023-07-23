package dev.iurysouza.livematch.playground

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import timber.log.Timber

class PlaygroundApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    Coil.setImageLoader {
      ImageLoader.Builder(this@PlaygroundApp)
        .components { add(SvgDecoder.Factory()) }
        .build()
    }
  }
}
