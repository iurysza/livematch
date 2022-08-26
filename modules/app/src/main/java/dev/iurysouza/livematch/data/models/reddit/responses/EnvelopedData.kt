package dev.iurysouza.livematch.data.models.reddit.responses

import dev.iurysouza.livematch.data.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.reddit.entities.base.Thing

interface EnvelopedData {

    val kind: EnvelopeKind

    val data: Thing
}
