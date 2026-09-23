package it.univaq.jsonplaceholder.domain.repositories

import it.univaq.jsonplaceholder.domain.model.User

interface LocalRepository {

    suspend fun save(data: List<User>)

    suspend fun getAll(): List<User>

    suspend fun clear()
}