package dev.iurysouza.livematch.data.repo


//import androidx.test.runner.AndroidJUnit4

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dev.iurysouza.livematch.data.RedditApi
import javax.inject.Inject
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AndroidXJunitTest {


    @Inject
    lateinit var redditApi: RedditApi

    companion object {
        @get:Rule
        val rule = HiltAndroidRule(this)

        @BeforeClass
        fun setup() {
            rule.inject()
        }
    }

    @Test
    fun testing() {
        // Testing...
        println()
    }

}

