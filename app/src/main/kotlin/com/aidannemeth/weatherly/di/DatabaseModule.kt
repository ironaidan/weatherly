package com.aidannemeth.weatherly.di

import android.content.Context
import androidx.room.Room
import com.aidannemeth.weatherly.db.AppDatabase
import com.aidannemeth.weatherly.feature.weather.data.local.WeatherDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "db-weatherly"

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseModule {
    @Binds
    fun provideWeatherDatabase(appDatabase: AppDatabase): WeatherDatabase

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = DATABASE_NAME,
            ).build()
        }
    }
}
