package it.univaq.speedcamerafinder.data.remote.service

import it.univaq.speedcamerafinder.data.remote.model.RemoteUser
import retrofit2.http.GET

interface EndpointService {

    @GET("users")
    suspend fun users(): List<RemoteUser>
}