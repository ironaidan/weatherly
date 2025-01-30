@file:Suppress("unused")

package com.aidannemeth.weatherly.di

import android.content.Context
import androidx.room.Room
import com.aidannemeth.weatherly.db.AppDatabase
import com.aidannemeth.weatherly.feature.weather.data.local.WeatherDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
interface DatabaseTestModule {
    @Binds
    fun provideWeatherDatabase(appDatabase: AppDatabase): WeatherDatabase

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
            Room.inMemoryDatabaseBuilder(
                context,
                AppDatabase::class.java,
            ).build()
    }
}
