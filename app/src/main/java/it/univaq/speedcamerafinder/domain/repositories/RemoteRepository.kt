package it.univaq.speedcamerafinder.domain.repositories

import it.univaq.speedcamerafinder.domain.model.SpeedCamera

interface RemoteRepository {

    suspend fun downloadData(): List<SpeedCamera>
}