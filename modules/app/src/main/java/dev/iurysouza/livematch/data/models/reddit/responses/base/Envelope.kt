package dev.iurysouza.livematch.data.models.reddit.responses.base

interface Envelope<T> {

    val kind: EnvelopeKind?

    val data: T?
}
