package dev.iurysouza.livematch.domain.adapters.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchThreadEntity(
    val id: String,
    val title: String,
    val url: String,
    val score: Int,
    val content: String,
    val contentHtml: String,
    val numComments: Int,
    val createdAt: Long,
) : Parcelable

