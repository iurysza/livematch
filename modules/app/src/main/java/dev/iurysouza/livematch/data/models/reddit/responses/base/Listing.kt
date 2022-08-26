package dev.iurysouza.livematch.data.models.reddit.responses.base

interface Listing<T> {

    val modhash: String?
    val dist: Int?

    val children: List<T>

    val after: String?
    val before: String?
}
