package dev.iurysouza.livematch.domain.models.reddit.responses

import dev.iurysouza.livematch.domain.models.reddit.entities.base.Thing
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind

interface EnvelopedData {

    val kind: EnvelopeKind

    val data: Thing
}
