package it.univaq.speedcamerafinder.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.univaq.speedcamerafinder.common.LocationHelper
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun providerLocation(@ApplicationContext context: Context) = LocationHelper(context)
}