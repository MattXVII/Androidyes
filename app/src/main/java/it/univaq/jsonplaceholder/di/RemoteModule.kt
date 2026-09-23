package it.univaq.jsonplaceholder.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.univaq.jsonplaceholder.data.remote.RetrofitRemoteRepository
import it.univaq.jsonplaceholder.data.remote.service.EndpointService
import it.univaq.jsonplaceholder.domain.repositories.RemoteRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides @Singleton
    fun provideClient() = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun service(client: Retrofit) = client.create(EndpointService::class.java)
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