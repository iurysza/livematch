package dev.iurysouza.livematch.data.models.cloned.responses

import android.os.Parcelable
import dev.iurysouza.livematch.data.models.cloned.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.cloned.models.base.CommentData

interface EnvelopedCommentData : EnvelopedContribution, Parcelable {

    override val kind: EnvelopeKind

    override val data: CommentData
}
