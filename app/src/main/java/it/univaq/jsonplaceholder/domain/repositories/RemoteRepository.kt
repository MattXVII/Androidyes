package it.univaq.jsonplaceholder.domain.repositories

import it.univaq.jsonplaceholder.domain.model.User

interface RemoteRepository {

    suspend fun downloadData(): List<User>
}