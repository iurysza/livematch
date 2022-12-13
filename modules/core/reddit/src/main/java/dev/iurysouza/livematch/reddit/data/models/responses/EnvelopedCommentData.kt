package dev.iurysouza.livematch.reddit.data.models.responses

import android.os.Parcelable
import dev.iurysouza.livematch.reddit.data.models.entities.base.CommentData
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind

interface EnvelopedCommentData : EnvelopedContribution, Parcelable {

  override val kind: EnvelopeKind

  override val data: CommentData
}
