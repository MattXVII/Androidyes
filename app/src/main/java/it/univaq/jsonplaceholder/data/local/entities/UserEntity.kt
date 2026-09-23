package it.univaq.jsonplaceholder.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String,
    val username: String,
    val email: String,
    val city: String,
    val lat: Double,
    val lng: Double
)