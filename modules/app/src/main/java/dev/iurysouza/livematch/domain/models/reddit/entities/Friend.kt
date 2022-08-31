package dev.iurysouza.livematch.domain.models.reddit.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.domain.models.reddit.entities.base.Thing
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Friend(

    @Json(name = "id")
    override val id: String,

    @Json(name = "name")
    override val fullname: String,

    @Json(name = "rel_id")
    val relId: String,

    @Json(name = "date")
    val added: Long,

    ) : Thing, Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class FriendList(

    @Json(name = "kind")
    val kind: EnvelopeKind,

    val data: FriendListData,

    ) : Parcelable

@JsonClass(generateAdapter = true)
@Parcelize
data class FriendListData(

    val children: List<Friend>,

    ) : Parcelable
