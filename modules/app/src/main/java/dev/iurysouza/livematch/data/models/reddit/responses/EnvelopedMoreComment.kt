package dev.iurysouza.livematch.data.models.reddit.responses

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.data.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.reddit.entities.MoreComments
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class EnvelopedMoreComment(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: MoreComments,

    ) : Envelope<MoreComments>, EnvelopedCommentData, Parcelable
