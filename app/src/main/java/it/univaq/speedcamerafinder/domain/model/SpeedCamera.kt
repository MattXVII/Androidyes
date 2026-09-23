package it.univaq.speedcamerafinder.domain.model

data class SpeedCamera(
    val id: Long,
    val lat: Double,
    val lng: Double,
    val maxSpeed: Int?,
    val direction: String?
)
