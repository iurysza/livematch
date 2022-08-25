package dev.iurysouza.livematch.data.models.cloned.responses.listings

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.cloned.responses.base.Listing
import dev.iurysouza.livematch.data.models.cloned.models.WikiRevision
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class WikiRevisionListing(

    @Json(name = "modhash")
    override val modhash: String?,
    @Json(name = "dist")
    override val dist: Int?,

    @Json(name = "children")
    override val children: List<WikiRevision>,

    @Json(name = "after")
    override val after: String?,
    @Json(name = "before")
    override val before: String?,

    ) : Listing<WikiRevision>, Parcelable
