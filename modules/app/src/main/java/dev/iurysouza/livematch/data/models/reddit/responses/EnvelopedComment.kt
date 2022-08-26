package dev.iurysouza.livematch.data.models.reddit.responses

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.reddit.responses.base.Envelope
import dev.iurysouza.livematch.data.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.reddit.entities.Comment
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class EnvelopedComment(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: Comment,

    ) : Envelope<Comment>, EnvelopedCommentData, Parcelable
