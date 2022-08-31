package dev.iurysouza.livematch.fakes

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import dev.iurysouza.livematch.BuildConfig
import dev.iurysouza.livematch.data.PolyJsonAdapterFactory
import dev.iurysouza.livematch.domain.models.reddit.entities.PremiumSubreddit
import dev.iurysouza.livematch.domain.models.reddit.entities.PrivateSubreddit
import dev.iurysouza.livematch.domain.models.reddit.entities.Redditor
import dev.iurysouza.livematch.domain.models.reddit.entities.Subreddit
import dev.iurysouza.livematch.domain.models.reddit.entities.SuspendedRedditor
import dev.iurysouza.livematch.domain.models.reddit.entities.base.RedditorData
import dev.iurysouza.livematch.domain.models.reddit.entities.base.SubredditData
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedComment
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedCommentData
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedContribution
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedData
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedMessage
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedMoreComment
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedRedditor
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedSubmission
import dev.iurysouza.livematch.domain.models.reddit.responses.EnvelopedSubreddit
import dev.iurysouza.livematch.domain.models.reddit.responses.base.EnvelopeKind
import dev.iurysouza.livematch.data.network.RedditApi
import java.util.Base64
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object FakeRedditApi {
    fun createWith(mockWebServer: MockWebServer): RedditApi {
        val username = BuildConfig.CLIENT_ID
        val password = ""
        val credentials = "Basic ${
            Base64.getEncoder().encodeToString(
                "$username:$password".toByteArray()
            )
        }"
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request()
                        .newBuilder()
                        .addHeader("Authorization", credentials).build()
                )
            }.build()

        val factory = MoshiConverterFactory.create(provideMoshi())

        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(factory)
            .client(okhttpClient)
            .build().create(RedditApi::class.java)
    }

    private fun provideMoshi(): Moshi = Moshi.Builder().apply {
        add(
            PolymorphicJsonAdapterFactory.of(EnvelopedData::class.java, "kind")
                .withSubtype(EnvelopedComment::class.java, EnvelopeKind.Comment.value)
                .withSubtype(EnvelopedMoreComment::class.java, EnvelopeKind.More.value)
                .withSubtype(EnvelopedMessage::class.java, EnvelopeKind.Message.value)
                .withSubtype(EnvelopedRedditor::class.java, EnvelopeKind.Account.value)
                .withSubtype(EnvelopedSubmission::class.java, EnvelopeKind.Link.value)
                .withSubtype(EnvelopedSubreddit::class.java, EnvelopeKind.Subreddit.value)
        )
            .add(
                PolymorphicJsonAdapterFactory.of(EnvelopedContribution::class.java, "kind")
                    .withSubtype(EnvelopedSubmission::class.java, EnvelopeKind.Link.value)
                    .withSubtype(EnvelopedComment::class.java, EnvelopeKind.Comment.value)
                    .withSubtype(EnvelopedMoreComment::class.java, EnvelopeKind.More.value)
            )
            .add(
                PolymorphicJsonAdapterFactory.of(EnvelopedCommentData::class.java, "kind")
                    .withSubtype(EnvelopedComment::class.java, EnvelopeKind.Comment.value)
                    .withSubtype(EnvelopedMoreComment::class.java, EnvelopeKind.More.value)
            )
            .add(
                PolymorphicJsonAdapterFactory.of(SubredditData::class.java, "subreddit_type")
                    .withSubtype(PremiumSubreddit::class.java, "gold_only")
                    .withSubtype(PremiumSubreddit::class.java, "gold_restricted")
                    .withSubtype(Subreddit::class.java, "archived")
                    .withSubtype(Subreddit::class.java, "public")
                    .withSubtype(Subreddit::class.java, "restricted")
                    .withSubtype(Subreddit::class.java, "user")
                    .withSubtype(PrivateSubreddit::class.java, "private")
            )
            .add(
                PolyJsonAdapterFactory(
                    baseType = RedditorData::class.java,
                    possibleTypes = arrayOf(Redditor::class.java, SuspendedRedditor::class.java),
                    selectType = { label, value ->

                        when {
                            label == "is_suspended" && value == true -> SuspendedRedditor::class.java
                            label == "has_verified_email" -> Redditor::class.java
                            else -> null
                        }
                    }
                )
            )
    }.build()
}
