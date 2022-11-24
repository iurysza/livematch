package dev.iurysouza.livematch.ui.features.matchthread

import android.os.Parcelable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.iurysouza.livematch.ui.theme.LiveMatchAssets
import dev.iurysouza.livematch.ui.theme.livematchassets.Booking
import dev.iurysouza.livematch.ui.theme.livematchassets.Clock
import dev.iurysouza.livematch.ui.theme.livematchassets.Goal
import dev.iurysouza.livematch.ui.theme.livematchassets.Sub
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class EventIcon(val icon: String) : Parcelable {
    object Goal : EventIcon("icon-ball")
    object YellowCard : EventIcon("icon-yellow")
    object RedCard : EventIcon("icon-red")
    object Substitution : EventIcon("icon-sub")
    object Penalty : EventIcon("icon-penalty")
    object OwnGoal : EventIcon("icon-own-goal")
    object Corner : EventIcon("icon-corner")
    object Offside : EventIcon("icon-offside")
    object FreeKick : EventIcon("icon-free-kick")
    object KickOff : EventIcon("icon-kickoff")
    object FinalWhistle : EventIcon("icon-final-whistle")
    object Foul : EventIcon("icon-foul")
    object Unknown : EventIcon("icon-unknown")

    companion object {
        fun fromString(icon: String): EventIcon = when (icon) {
            "icon-ball" -> Goal
            "icon-yellow" -> YellowCard
            "icon-red" -> RedCard
            "icon-sub" -> Substitution
            "icon-penalty" -> Penalty
            "icon-own-goal" -> OwnGoal
            "icon-corner" -> Corner
            "icon-offside" -> Offside
            "icon-free-kick" -> FreeKick
            "icon-throw-in" -> KickOff
            "icon-handball" -> FinalWhistle
            "icon-foul" -> Foul
            else -> Unknown
        }
    }

    fun toImageVector(): ImageVector = when (this) {
        is Goal -> LiveMatchAssets.Goal
        is Foul -> LiveMatchAssets.Goal
        is Corner -> LiveMatchAssets.Goal
        is Penalty -> LiveMatchAssets.Goal
        is OwnGoal -> LiveMatchAssets.Goal
        is Offside -> LiveMatchAssets.Goal
        is KickOff -> LiveMatchAssets.Goal
        is Unknown -> LiveMatchAssets.Goal
        is FreeKick -> LiveMatchAssets.Goal
        is RedCard -> LiveMatchAssets.Booking
        is Substitution -> LiveMatchAssets.Sub
        is FinalWhistle -> LiveMatchAssets.Clock
        is YellowCard -> LiveMatchAssets.Booking
    }
}
