package com.aidannemeth.weatherly.feature.weather.data.repository

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherLocalDataSource
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
) : WeatherRepository {
    override suspend fun getLocalWeather(): Either<DataError.Local, Weather> =
        weatherLocalDataSource.getWeather()

    override fun observeWeather(): Flow<Either<DataError, Weather>> =
        weatherLocalDataSource.observeWeather()
}
