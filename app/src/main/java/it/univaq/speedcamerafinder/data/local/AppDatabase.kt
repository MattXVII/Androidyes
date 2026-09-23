package it.univaq.speedcamerafinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import it.univaq.speedcamerafinder.data.local.entities.SpeedCameraEntity

@Database(entities = [SpeedCameraEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun speedCameraDao(): SpeedCameraDao
}
