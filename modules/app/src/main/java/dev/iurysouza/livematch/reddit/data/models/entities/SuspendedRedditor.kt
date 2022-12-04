package dev.iurysouza.livematch.reddit.data.models.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.entities.base.RedditorData
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class SuspendedRedditor(

    @Json(name = "name")
    override val fullname: String,

    @Json(name = "awarder_karma")
    val awarderKarma: Int?,

    @Json(name = "awardee_karma")
    val awardeeKarma: Int?,

    @Json(name = "is_suspended")
    val isSuspended: Boolean,

    @Json(name = "total_karma")
    val totalKarma: Int?,

    ) : RedditorData, Parcelable
