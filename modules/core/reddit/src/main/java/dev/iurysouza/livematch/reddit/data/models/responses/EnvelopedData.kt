package dev.iurysouza.livematch.reddit.data.models.responses

import dev.iurysouza.livematch.reddit.data.models.entities.base.Thing
import dev.iurysouza.livematch.reddit.data.models.responses.base.EnvelopeKind

interface EnvelopedData {

  val kind: EnvelopeKind

  val data: Thing
}
