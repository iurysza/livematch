package dev.iurysouza.livematch.data.models.reddit.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class UploadFile(

    @Json(name = "file")
    val file: String,

    ) : Parcelable