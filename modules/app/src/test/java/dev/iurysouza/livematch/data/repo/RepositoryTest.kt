package dev.iurysouza.livematch.data.repo

import dev.iurysouza.livematch.data.PlaceHolderApi
import dev.iurysouza.livematch.data.models.NetworkResponse
import dev.iurysouza.livematch.util.CoroutineTestRule
import dev.iurysouza.livematch.util.runBlockingTest
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest {

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    private lateinit var repository: Repository
    private val apiMock = mockk<PlaceHolderApi>()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        clearAllMocks()
        repository = Repository(
            dispatchers = coroutineRule.testDispatcherProvider,
            api = apiMock,
        )
    }


    @Test
    fun `When request to posts api throws an error then getPosts should emit error state`() =
        coroutineRule.runBlockingTest {
            coEvery {
                apiMock.getPosts()
            } throws IllegalStateException()

            val firstEmittedItem = repository.getPosts().toList().first()

            firstEmittedItem.status shouldBe NetworkResponse.Status.ERROR
        }

    @Test
    fun `When request to post api returns a valid object then repository should emit success state`() =
        coroutineRule.runBlockingTest {
            coEvery {
                apiMock.getPosts()
            } returns Fixtures.Entity.fakePostList

            val firstEmittedItem = repository.getPosts().toList().first()

            firstEmittedItem.status shouldBe NetworkResponse.Status.SUCCESS
        }

    @Test
    fun `When request to users api throws an error then getPosts should emit error state`() =
        coroutineRule.runBlockingTest {
            coEvery {
                apiMock.getUsers()
            } throws IllegalStateException()

            val firstEmittedItem = repository.getUsers().toList().first()

            firstEmittedItem.status shouldBe NetworkResponse.Status.ERROR
        }

    @Test
    fun `When request to users api returns a valid object then repository should emit success state`() =
        coroutineRule.runBlockingTest {
            coEvery {
                apiMock.getUsers()
            } returns Fixtures.Entity.fakeUserList

            val firstEmittedItem = repository.getUsers().toList().first()

            firstEmittedItem.status shouldBe NetworkResponse.Status.SUCCESS
        }
}
