package dev.iurysouza.livematch.domain.models.reddit.responses

import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.entities.base.Contribution

interface EnvelopedContribution : EnvelopedData {

    override val kind: EnvelopeKind

    override val data: Contribution
}
