package dev.iurysouza.livematch.data.models.cloned.responses.base

interface Envelope<T> {

    val kind: EnvelopeKind?

    val data: T?
}
