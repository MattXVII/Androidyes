package it.univaq.jsonplaceholder.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import it.univaq.jsonplaceholder.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Upsert
    suspend fun insert(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>

    @Query("DELETE FROM users")
    suspend fun clear()

}