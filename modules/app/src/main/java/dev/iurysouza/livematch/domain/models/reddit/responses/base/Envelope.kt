package dev.iurysouza.livematch.domain.models.reddit.responses.base

interface Envelope<T> {

    val kind: EnvelopeKind?

    val data: T?
}
