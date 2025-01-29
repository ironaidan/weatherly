package com.aidannemeth.weatherly.feature.weather.dagger

import com.aidannemeth.weatherly.feature.weather.data.local.WeatherLocalDataSourceImpl
import com.aidannemeth.weatherly.feature.weather.data.remote.WeatherRemoteDataSourceImpl
import com.aidannemeth.weatherly.feature.weather.data.repository.WeatherRepositoryImpl
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
    fun bindWeatherLocalDataSource(
        impl: WeatherLocalDataSourceImpl,
    ): WeatherLocalDataSource

    @Binds
    @Singleton
    fun bindWeatherRemoteDataSource(
        impl: WeatherRemoteDataSourceImpl,
    ): WeatherRemoteDataSource

    @Binds
    @Singleton
    fun bindWeatherRepository(
        impl: WeatherRepositoryImpl,
    ): WeatherRepository
}
