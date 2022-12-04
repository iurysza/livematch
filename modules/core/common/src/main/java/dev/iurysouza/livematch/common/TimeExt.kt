package dev.iurysouza.livematch.common

import java.time.Instant

fun Long.isInTheFuture() = Instant.ofEpochMilli(this).isAfter(Instant.now())
fun nowPlusMillis(expiresIn: Long) = Instant.now().plusMillis(expiresIn).toEpochMilli()
fun Int.secondsFromNow() = Instant.now().plusSeconds(this.toLong()).toEpochMilli()
fun Int.secondsAgo() = Instant.now().minusSeconds(this.toLong()).toEpochMilli()
