package dev.iurysouza.livematch.data.legacy

data class UserEntity(
    val address: AddressEntity,
    val company: CompanyEntity,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String,
)
