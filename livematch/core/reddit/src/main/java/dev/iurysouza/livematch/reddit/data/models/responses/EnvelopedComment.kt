package dev.iurysouza.livematch.reddit.data.models.responses

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.entities.Comment
import dev.iurysouza.livematch.reddit.data.models.responses.base.Envelope
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
class EnvelopedComment(

  @Json(name = "kind")
  override val kind: EnvelopeKind,

  @Json(name = "data")
  override val data: Comment,

) : Envelope<Comment>, EnvelopedCommentData, Parcelable
