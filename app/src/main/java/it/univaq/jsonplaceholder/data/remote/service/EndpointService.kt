package it.univaq.jsonplaceholder.data.remote.service

import it.univaq.jsonplaceholder.data.remote.model.RemoteUser
import retrofit2.http.GET

interface EndpointService {

    @GET("users")
    suspend fun users(): List<RemoteUser>
}