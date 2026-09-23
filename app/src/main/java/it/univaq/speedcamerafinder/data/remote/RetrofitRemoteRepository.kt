package it.univaq.speedcamerafinder.data.remote

import it.univaq.speedcamerafinder.data.remote.model.OverpassElement
import it.univaq.speedcamerafinder.data.remote.service.OverpassService
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
import javax.inject.Inject

// maxspeed può essere "50", "50 mph" o "IT:urban": teniamo solo i numeri
private fun OverpassElement.toDomain() = SpeedCamera(
    id = id,
    lat = lat,
    lng = lon,
    maxSpeed = tags?.get("maxspeed")?.substringBefore(" ")?.toIntOrNull(),
    direction = tags?.get("direction"),
    name = tags?.get("name")
)

class RetrofitRemoteRepository @Inject constructor(
    private val service: OverpassService
): RemoteRepository {

    override suspend fun downloadData(lat: Double, lng: Double, radius: Int): List<SpeedCamera> {
        val query = "[out:json][timeout:25];" +
                "node[\"highway\"=\"speed_camera\"](around:$radius,$lat,$lng);" +
                "out;"
        return service.query(query).elements.map { it.toDomain() }
    }
}
