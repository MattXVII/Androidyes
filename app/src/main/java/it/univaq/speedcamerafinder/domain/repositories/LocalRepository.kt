package it.univaq.speedcamerafinder.domain.repositories

import it.univaq.speedcamerafinder.domain.model.User

interface LocalRepository {

    suspend fun save(data: List<User>)

    suspend fun getAll(): List<User>

    suspend fun clear()
}