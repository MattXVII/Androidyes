package it.univaq.speedcamerafinder.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "speed_cameras")
data class SpeedCameraEntity(
    @PrimaryKey val id: Long,
    val lat: Double,
    val lng: Double,
    val maxSpeed: Int?,
    val direction: String?,
    val name: String?
)
