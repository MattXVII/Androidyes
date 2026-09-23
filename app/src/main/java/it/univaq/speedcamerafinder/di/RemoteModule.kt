package it.univaq.speedcamerafinder.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.univaq.speedcamerafinder.data.remote.FakeRemoteRepository
import it.univaq.speedcamerafinder.domain.repositories.RemoteRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        fakeRemoteRepository: FakeRemoteRepository
    ): RemoteRepository

}
