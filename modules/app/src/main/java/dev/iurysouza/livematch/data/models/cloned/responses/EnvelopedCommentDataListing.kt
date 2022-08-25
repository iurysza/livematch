package dev.iurysouza.livematch.data.models.cloned.responses

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.cloned.responses.base.Envelope
import dev.iurysouza.livematch.data.models.cloned.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.cloned.responses.listings.CommentDataListing
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class EnvelopedCommentDataListing(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: CommentDataListing,

    ) : Envelope<CommentDataListing>, Parcelable
