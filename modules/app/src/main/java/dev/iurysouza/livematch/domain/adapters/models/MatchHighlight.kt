package dev.iurysouza.livematch.domain.adapters.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MatchHighlight(
    val parentId: String,
    val title: String?,
    val type: String?,
    val html: String?,
    val providerName: String?,
    val providerUrl: String?,
    val authorName: String?,
    val authorUrl: String?,
    val thumbnailUrl: String?,
    val thumbnailWidth: Int?,
    val thumbnailHeight: Int?,
    val width: Int?,
    val height: Int?,
    val createdAt: Long,
) : Parcelable
