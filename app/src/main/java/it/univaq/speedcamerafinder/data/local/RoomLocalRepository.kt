package it.univaq.speedcamerafinder.data.local

import it.univaq.speedcamerafinder.data.local.entities.SpeedCameraEntity
import it.univaq.speedcamerafinder.domain.model.SpeedCamera
import it.univaq.speedcamerafinder.domain.repositories.LocalRepository
import javax.inject.Inject

private fun SpeedCamera.toEntity() = SpeedCameraEntity(
    id = id,
    lat = lat,
    lng = lng,
    maxSpeed = maxSpeed,
    direction = direction
)

private fun SpeedCameraEntity.toDomain() = SpeedCamera(
    id = id,
    lat = lat,
    lng = lng,
    maxSpeed = maxSpeed,
    direction = direction
)

class RoomLocalRepository @Inject constructor(
    private val speedCameraDao: SpeedCameraDao
): LocalRepository {

    // Room esegue già le funzioni suspend del DAO su un thread di background
    override suspend fun save(data: List<SpeedCamera>) {
        clear()
        speedCameraDao.insert(data.map { it.toEntity() })
    }

    override suspend fun getAll(): List<SpeedCamera> {
        return speedCameraDao.getAll().map { it.toDomain() }
    }

    override suspend fun clear() {
        speedCameraDao.clear()
    }
}
