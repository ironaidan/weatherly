package com.aidannemeth.weatherly.di

import android.content.Context
import androidx.room.Room
import com.aidannemeth.weatherly.db.WeatherlyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "db-weatherly"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): WeatherlyDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = WeatherlyDatabase::class.java,
            name = DATABASE_NAME,
        ).build()
    }
}
