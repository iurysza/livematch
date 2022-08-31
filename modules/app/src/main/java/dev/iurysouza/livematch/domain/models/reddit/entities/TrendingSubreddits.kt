package dev.iurysouza.livematch.domain.models.reddit.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class TrendingSubreddits(

    @Json(name = "subreddit_names")
    val subredditNames: List<String>,

    @Json(name = "comment_count")
    val commentCount: Int,

    @Json(name = "comment_url")
    val commentUrl: String,

    ) : Parcelable
