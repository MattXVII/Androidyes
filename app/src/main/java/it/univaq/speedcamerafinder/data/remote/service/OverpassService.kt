package it.univaq.speedcamerafinder.data.remote.service

import it.univaq.speedcamerafinder.data.remote.model.OverpassResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OverpassService {

    @FormUrlEncoded
    @POST("api/interpreter")
    suspend fun query(@Field("data") query: String): OverpassResponse
}
