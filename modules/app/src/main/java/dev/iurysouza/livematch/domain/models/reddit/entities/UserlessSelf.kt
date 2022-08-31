package dev.iurysouza.livematch.domain.models.reddit.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@JsonClass(generateAdapter = true)
@Parcelize
data class UserlessSelf(

    @Json(name = "features")
    val features: @RawValue Map<String, Any>,

    ) : Parcelable
