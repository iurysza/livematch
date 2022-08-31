package dev.iurysouza.livematch.domain.models.reddit.responses

import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.entities.base.Thing

interface EnvelopedData {

    val kind: EnvelopeKind

    val data: Thing
}
