package dev.iurysouza.livematch.data.models.cloned.responses.base

interface Listing<T> {

    val modhash: String?
    val dist: Int?

    val children: List<T>

    val after: String?
    val before: String?
}
