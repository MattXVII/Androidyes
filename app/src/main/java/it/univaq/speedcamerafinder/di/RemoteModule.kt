package it.univaq.speedcamerafinder.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.univaq.speedcamerafinder.data.remote.RetrofitRemoteRepository
import it.univaq.speedcamerafinder.data.remote.service.OverpassService
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    // Overpass rifiuta (HTTP 406) le richieste senza uno User-Agent che identifichi l'app
    @Provides @Singleton
    fun provideHttpClient() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("User-Agent", "SpeedCameraFinder/1.0 (progetto universitario UnivAQ)")
                    .build()
            )
        }
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides @Singleton
    fun provideClient(httpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl("https://overpass-api.de/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun service(client: Retrofit) = client.create(OverpassService::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        retrofitRemoteRepository: RetrofitRemoteRepository
    ): RemoteRepository

}
