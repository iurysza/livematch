package dev.iurysouza.livematch.reddit.data.models.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.reddit.data.models.entities.base.Thing
import dev.iurysouza.livematch.reddit.data.models.entities.commons.ImageDetail
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Awarding(

  @Json(name = "id")
  override val id: String,

  @Json(name = "name")
  override val fullname: String,

  @Json(name = "award_type")
  val awardType: String,

  @Json(name = "count")
  val count: Int,

  @Json(name = "coin_price")
  val coinPrice: Int,

  @Json(name = "coin_reward")
  val coinReward: Int,

  @Json(name = "description")
  val description: String,

  @Json(name = "icon_url")
  val iconUrl: String,

  @Json(name = "icon_width")
  val iconWidth: Int,

  @Json(name = "icon_height")
  val iconHeight: Int,

  @Json(name = "resized_icons")
  val resizedIcons: List<ImageDetail>,

  @Json(name = "static_icon_url")
  val staticIconUrl: String,

  @Json(name = "static_icon_width")
  val staticIconWidth: Int,

  @Json(name = "static_icon_height")
  val staticIconHeight: Int,

  @Json(name = "resized_static_icons")
  val resizedStaticIcons: List<ImageDetail>,

  @Json(name = "is_enabled")
  val isEnabled: Boolean,

) : Thing, Parcelable
