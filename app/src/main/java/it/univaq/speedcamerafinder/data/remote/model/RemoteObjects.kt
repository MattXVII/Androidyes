package it.univaq.speedcamerafinder.data.remote.model

// Risposta JSON di Overpass: { "elements": [ { "id", "lat", "lon", "tags": {...} } ] }
data class OverpassResponse(
    val elements: List<OverpassElement>
)

data class OverpassElement(
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: Map<String, String>?
)
