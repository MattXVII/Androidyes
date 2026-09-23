package it.univaq.jsonplaceholder.data.remote.model

data class RemoteUser(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: RemoteAddress,
    val phone: String,
    val website: String,
    val company: RemoteCompany
)

data class RemoteAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: RemoteGeo
)

data class RemoteGeo(
    val lat: String,
    val lng: String
)

data class RemoteCompany(
    val name: String,
    val catchPhrase: String,
    val bs: String
)