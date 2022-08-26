package dev.iurysouza.livematch.data.models.reddit.responses

import android.os.Parcelable
import dev.iurysouza.livematch.data.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.reddit.entities.base.CommentData

interface EnvelopedCommentData : EnvelopedContribution, Parcelable {

    override val kind: EnvelopeKind

    override val data: CommentData
}
