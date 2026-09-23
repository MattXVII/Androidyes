package it.univaq.speedcamerafinder.data.remote

import it.univaq.speedcamerafinder.data.remote.model.OverpassElement
import it.univaq.speedcamerafinder.data.remote.service.OverpassService
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
import javax.inject.Inject

private fun OverpassElement.toDomain() = SpeedCamera(
    id = id,
    lat = lat,
    lng = lon,
    maxSpeed = tags?.get("maxspeed")?.toIntOrNull(),
    direction = tags?.get("direction")
)

class RetrofitRemoteRepository @Inject constructor(
    private val service: OverpassService
): RemoteRepository {

    override suspend fun downloadData(lat: Double, lng: Double, radius: Int): List<SpeedCamera> {
        val query = "[out:json];" +
                "node[\"highway\"=\"speed_camera\"](around:$radius,$lat,$lng);" +
                "out;"
        return service.query(query).elements.map { it.toDomain() }
    }
}
