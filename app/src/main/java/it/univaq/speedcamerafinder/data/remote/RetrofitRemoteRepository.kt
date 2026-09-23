package it.univaq.speedcamerafinder.data.remote

import it.univaq.speedcamerafinder.data.remote.model.RemoteUser
import it.univaq.speedcamerafinder.data.remote.service.EndpointService
import it.univaq.speedcamerafinder.domain.model.User
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
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