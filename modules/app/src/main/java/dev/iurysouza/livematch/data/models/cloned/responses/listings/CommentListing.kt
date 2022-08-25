package dev.iurysouza.livematch.data.models.cloned.responses.listings

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.cloned.responses.EnvelopedComment
import dev.iurysouza.livematch.data.models.cloned.responses.base.Listing
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class CommentListing(

    @Json(name = "modhash")
    override val modhash: String?,
    @Json(name = "dist")
    override val dist: Int?,

    @Json(name = "children")
    override val children: List<EnvelopedComment>,

    @Json(name = "after")
    override val after: String?,
    @Json(name = "before")
    override val before: String?,

    ) : Listing<EnvelopedComment>, Parcelable
