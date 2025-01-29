package com.aidannemeth.weatherly.feature.weather.dagger

import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface WeatherModule {
    @Binds
    @Singleton
    fun bindWeatherLocalDataSource(): WeatherLocalDataSource

    @Binds
    @Singleton
    fun bindWeatherRemoteDataSource(): WeatherRemoteDataSource

    @Binds
    @Singleton
    fun bindWeatherRepository(): WeatherRepository
}
