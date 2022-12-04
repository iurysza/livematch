package dev.iurysouza.livematch.reddit.data.models.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.entities.Submission
import dev.iurysouza.livematch.reddit.data.models.responses.base.Envelope
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind

@JsonClass(generateAdapter = true)
class EnvelopedSubmission(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: Submission,

    ) : Envelope<Submission>, EnvelopedContribution
