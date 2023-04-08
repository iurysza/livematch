package dev.iurysouza.livematch.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import dev.iurysouza.livematch.matchthread.models.MatchThread as TargetMatchThread

sealed class Destination : Parcelable {

  @Parcelize
  object MatchDay : Destination()

  @Parcelize
  object Error : Destination()

  @Parcelize
  data class MatchThread(val matchThread: TargetMatchThread) : Destination()

}
