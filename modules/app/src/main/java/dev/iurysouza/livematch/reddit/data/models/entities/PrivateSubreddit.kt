package dev.iurysouza.livematch.reddit.data.models.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.entities.base.SubredditData
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class PrivateSubreddit(

    @Json(name = "id")
    override val id: String,

    @Json(name = "name")
    override val fullname: String,

    @Json(name = "community_icon")
    val communityIcon: String,

    @Json(name = "created")
    override val created: Long,

    @Json(name = "created_utc")
    override val createdUtc: Long,

    @Json(name = "display_name")
    override val displayName: String,

    @Json(name = "display_name_prefixed")
    override val displayNamePrefixed: String,

    @Json(name = "public_description")
    val publicDescription: String,

    @Json(name = "public_description_html")
    val publicDescriptionHtml: String?,

    @Json(name = "subreddit_type")
    override val subredditType: String,

    @Json(name = "title")
    override val title: String,

    @Json(name = "url")
    override val url: String,

    ) : SubredditData, Parcelable
