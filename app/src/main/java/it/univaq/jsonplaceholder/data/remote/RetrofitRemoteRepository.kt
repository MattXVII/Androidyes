package it.univaq.jsonplaceholder.data.remote

import it.univaq.jsonplaceholder.data.remote.model.RemoteUser
import it.univaq.jsonplaceholder.data.remote.service.EndpointService
import it.univaq.jsonplaceholder.domain.model.User
import it.univaq.jsonplaceholder.domain.repositories.RemoteRepository
import javax.inject.Inject

private fun RemoteUser.toDomain() = User(
    name = name,
    username = username,
    email = email,
    city = address.city,
    lat = address.geo.lat.toDoubleOrNull() ?: 0.0,
    lng = address.geo.lng.toDoubleOrNull() ?: 0.0
)

class RetrofitRemoteRepository @Inject constructor(
    private val service: EndpointService
): RemoteRepository {

    override suspend fun downloadData(): List<User> {
        return service.users().map { it.toDomain() }
    }
}