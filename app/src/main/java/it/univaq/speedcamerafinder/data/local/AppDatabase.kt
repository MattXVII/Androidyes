package it.univaq.speedcamerafinder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import it.univaq.speedcamerafinder.data.local.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
}