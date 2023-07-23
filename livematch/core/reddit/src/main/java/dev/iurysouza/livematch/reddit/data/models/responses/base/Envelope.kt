package dev.iurysouza.livematch.reddit.data.models.responses.base

interface Envelope<T> {

  val kind: EnvelopeKind?

  val data: T?
}
