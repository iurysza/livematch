package dev.iurysouza.livematch.data.repo

import dev.iurysouza.livematch.data.models.AddressEntity
import dev.iurysouza.livematch.data.models.CompanyEntity
import dev.iurysouza.livematch.data.models.GeoEntity
import dev.iurysouza.livematch.data.models.PostEntity
import dev.iurysouza.livematch.data.models.UserEntity

object Fixtures {
    object Entity {
        val fakeUserList = listOf(
            UserEntity(
                address = AddressEntity(
                    city = "",
                    geo = GeoEntity(
                        lat = "",
                        lng = ""
                    ),
                    street = "",
                    suite = "",
                    zipcode = ""
                ),
                company = CompanyEntity(
                    bs = "",
                    catchPhrase = "",
                    name = ""
                ),
                id = 0,
                phone = "0000000",
                name = "name",
                username = "username",
                email = "name@email.com",
                website = "name.com"
            ),
        )
        val fakePostList = listOf(
            PostEntity(
                body = "do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                id = 0,
                userId = 0,
                title = "Lorem ipsum dolor sit amet",
            ),
        )
    }
}
