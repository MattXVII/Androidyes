package it.univaq.speedcamerafinder.domain.repositories

import it.univaq.speedcamerafinder.domain.model.SpeedCamera

interface RemoteRepository {

    suspend fun downloadData(lat: Double, lng: Double, radius: Int): List<SpeedCamera>
}
