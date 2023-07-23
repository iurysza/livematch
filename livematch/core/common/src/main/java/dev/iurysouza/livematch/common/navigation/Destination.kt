package dev.iurysouza.livematch.common.navigation

import android.os.Parcelable
import dev.iurysouza.livematch.common.navigation.models.MatchThreadArgs
import kotlinx.parcelize.Parcelize

sealed class Destination : Parcelable {

  @Parcelize
  object MatchDay : Destination()

  @Parcelize
  object Error : Destination()

  @Parcelize
  data class MatchThread(val matchThread: MatchThreadArgs) : Destination()
}
