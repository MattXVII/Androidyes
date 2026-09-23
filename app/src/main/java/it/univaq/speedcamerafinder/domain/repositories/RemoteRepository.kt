package it.univaq.speedcamerafinder.domain.repositories

import it.univaq.speedcamerafinder.domain.model.User

interface RemoteRepository {

    suspend fun downloadData(): List<User>
}