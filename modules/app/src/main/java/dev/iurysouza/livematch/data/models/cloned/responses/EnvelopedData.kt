package dev.iurysouza.livematch.data.models.cloned.responses

import dev.iurysouza.livematch.data.models.cloned.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.models.cloned.models.base.Thing

interface EnvelopedData {

    val kind: EnvelopeKind

    val data: Thing
}
