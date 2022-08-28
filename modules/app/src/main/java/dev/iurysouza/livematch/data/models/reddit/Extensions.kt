package dev.iurysouza.livematch.data.models.reddit

import dev.iurysouza.livematch.data.models.reddit.entities.Friend
import dev.iurysouza.livematch.data.models.reddit.entities.User
import dev.iurysouza.livematch.data.models.reddit.entities.WikiRevision
import dev.iurysouza.livematch.data.models.reddit.entities.base.Created
import dev.iurysouza.livematch.data.models.reddit.entities.base.Distinguishable
import dev.iurysouza.livematch.data.models.reddit.entities.base.Editable
import dev.iurysouza.livematch.data.models.reddit.entities.base.Votable
import dev.iurysouza.livematch.data.models.reddit.entities.enums.Distinguished
import dev.iurysouza.livematch.data.models.reddit.entities.enums.Vote
import java.util.Date

const val MILLIS = 1000L

/**
 * Useful extension to convert the creation date property,
 * from the Long unixtime, to a more practical Date object.
 * @return the Date format of the item creation date.
 */
val Created.createdDate: Date
    get() {
        val milliseconds = created * MILLIS
        return Date(milliseconds)
    }

/**
 * Useful extension to convert the utc creation date property,
 * from the Long unixtime, to a more practical Date object.
 * @return the Date format of the item utc creation date.
 */
val Created.createdUtcDate: Date
    get() {
        val milliseconds = createdUtc * MILLIS
        return Date(milliseconds)
    }

/**
 * Useful extension to map to an enum.
 * @return the Distinguished status of the item.
 */
val Distinguishable.distinguished: Distinguished
    get() {
        return when (distinguishedRaw) {
            "moderator" -> Distinguished.MODERATOR
            "admin" -> Distinguished.ADMIN
            "special" -> Distinguished.SPECIAL
            else -> Distinguished.NOT_DISTINGUISHED
        }
    }

/**
 * Useful extension to map to an enum.
 * @return the Vote status of the item.
 */
val Votable.vote: Vote
    get() {
        return when (likes) {
            true -> Vote.UPVOTE
            false -> Vote.DOWNVOTE
            else -> Vote.NONE
        }
    }

val Editable.hasEdited: Boolean
    get() {

        if (editedRaw is Long) {
            return true
        }

        if (editedRaw is Boolean) {
            return editedRaw as Boolean
        }

        return false
    }

val Editable.edited: Date
    get() {

        if (editedRaw is Long) {
            val milliseconds = (editedRaw as Long) / MILLIS
            return Date(milliseconds)
        }

        return Date()
    }

val Friend.addedDate: Date
    get() {
        val milliseconds = added * MILLIS
        return Date(milliseconds)
    }

val User.userDate: Date
    get() {
        val milliseconds = date * MILLIS
        return Date(milliseconds)
    }

val WikiRevision.timestampDate: Date
    get() {
        val milliseconds = timestamp * MILLIS
        return Date(milliseconds)
    }
