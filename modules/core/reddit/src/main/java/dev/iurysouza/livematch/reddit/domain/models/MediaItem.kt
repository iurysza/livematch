package dev.iurysouza.livematch.reddit.domain.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MediaItem(
    val title: String,
    val url: String,
) : Parcelable
