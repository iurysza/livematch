package dev.iurysouza.livematch.domain.models.reddit.responses

import android.os.Parcelable
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.entities.base.CommentData

interface EnvelopedCommentData : EnvelopedContribution, Parcelable {

    override val kind: EnvelopeKind

    override val data: CommentData
}
