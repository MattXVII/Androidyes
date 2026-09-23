package it.univaq.speedcamerafinder.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import it.univaq.speedcamerafinder.data.local.entities.SpeedCameraEntity

@Dao
interface SpeedCameraDao {

    @Upsert
    suspend fun insert(cameras: List<SpeedCameraEntity>)

    @Query("SELECT * FROM speed_cameras")
    suspend fun getAll(): List<SpeedCameraEntity>

    @Query("DELETE FROM speed_cameras")
    suspend fun clear()

}
