package com.aidannemeth.weatherly.feature.weather.dagger

import com.aidannemeth.weatherly.feature.weather.data.local.WeatherDatabase
import com.aidannemeth.weatherly.feature.weather.data.local.WeatherLocalDataSourceImpl
import com.aidannemeth.weatherly.feature.weather.data.repository.WeatherRepositoryImpl
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {
    @Provides
    @Singleton
    fun provideConversationLocalDataSource(
        db: WeatherDatabase,
    ): WeatherLocalDataSource =
        WeatherLocalDataSourceImpl(db)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherLocalDataSource: WeatherLocalDataSource,
    ): WeatherRepository =
        WeatherRepositoryImpl(weatherLocalDataSource)
}
