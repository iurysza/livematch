package dev.iurysouza.livematch.reddit.data.models.entities.commons

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class SubmissionPreview(

  @Json(name = "images")
  val images: Array<Images>,

  @Json(name = "reddit_video_preview")
  val redditVideoPreview: RedditVideo?,

  @Json(name = "enabled")
  val isEnabled: Boolean,

) : Parcelable {

  fun id(): String? {
    if (images.isEmpty()) {
      return null
    }

    return images.firstOrNull()?.id
  }

  fun source(): ImageDetail? {
    if (images.isEmpty()) {
      return null
    }

    return images.firstOrNull()?.source
  }

  fun lowerRes(): ImageDetail? {
    if (images.isEmpty()) {
      return null
    }

    return images.firstOrNull()?.resolutions?.firstOrNull()
  }

  fun higherRes(): ImageDetail? {
    if (images.isEmpty()) {
      return null
    }

    return images.firstOrNull()?.resolutions?.lastOrNull()
  }

  fun variants(): ImageVariants? {
    if (images.isEmpty()) {
      return null
    }

    return images.firstOrNull()?.variants
  }
}
