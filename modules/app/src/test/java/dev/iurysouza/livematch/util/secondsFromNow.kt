package dev.iurysouza.livematch.util

import java.time.Instant

fun Int.secondsFromNow() = Instant.now().plusSeconds(this.toLong()).toEpochMilli()
fun Int.secondsAgo() = Instant.now().minusSeconds(this.toLong()).toEpochMilli()
