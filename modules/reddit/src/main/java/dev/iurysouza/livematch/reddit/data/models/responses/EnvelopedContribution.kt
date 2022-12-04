package dev.iurysouza.livematch.reddit.data.models.responses

import dev.iurysouza.livematch.reddit.data.models.entities.base.Contribution
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind

interface EnvelopedContribution : EnvelopedData {

    override val kind: EnvelopeKind

    override val data: Contribution
}
