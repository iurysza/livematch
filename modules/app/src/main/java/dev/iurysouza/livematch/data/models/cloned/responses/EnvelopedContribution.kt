package dev.iurysouza.livematch.data.models.cloned.responses

import dev.iurysouza.livematch.data.models.cloned.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.cloned.models.base.Contribution

interface EnvelopedContribution : EnvelopedData {

    override val kind: EnvelopeKind

    override val data: Contribution
}
