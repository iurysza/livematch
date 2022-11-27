package dev.iurysouza.livematch.domain.models.reddit.responses

import dev.iurysouza.livematch.domain.models.reddit.entities.base.Contribution
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind

interface EnvelopedContribution : EnvelopedData {

    override val kind: EnvelopeKind

    override val data: Contribution
}
