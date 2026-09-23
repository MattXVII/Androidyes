package it.univaq.speedcamerafinder.domain.repositories

import it.univaq.speedcamerafinder.domain.model.SpeedCamera

interface LocalRepository {

    suspend fun save(data: List<SpeedCamera>)

    suspend fun getAll(): List<SpeedCamera>

    suspend fun clear()
}