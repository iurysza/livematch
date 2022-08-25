package dev.iurysouza.livematch.data.models.cloned.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.data.models.cloned.responses.base.Envelope
import dev.iurysouza.livematch.data.models.cloned.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.cloned.models.Submission

@JsonClass(generateAdapter = true)
class EnvelopedSubmission(

    @Json(name = "kind")
    override val kind: EnvelopeKind,

    @Json(name = "data")
    override val data: Submission,

    ) : Envelope<Submission>, EnvelopedContribution
