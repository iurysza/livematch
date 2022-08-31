package dev.iurysouza.livematch.domain.models.reddit.responses

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.responses.listings.CommentListing
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class EnvelopedCommentListing(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: CommentListing,

    ) : Envelope<CommentListing>, Parcelable
