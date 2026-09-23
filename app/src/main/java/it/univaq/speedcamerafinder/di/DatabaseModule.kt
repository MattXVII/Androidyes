package it.univaq.speedcamerafinder.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.univaq.speedcamerafinder.data.local.AppDatabase
import it.univaq.speedcamerafinder.data.local.RoomLocalRepository
import it.univaq.speedcamerafinder.domain.repositories.LocalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context)
        = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase) = database.userDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocalRepository(
        roomLocalRepository: RoomLocalRepository
    ): LocalRepository
}