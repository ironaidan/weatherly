package com.aidannemeth.weatherly.feature.weather.domain.repository

import arrow.core.Either
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun getWeather(): Either<DataError.Local.NoCachedData, Weather>

    fun observeWeather(): Flow<Either<DataError.Local.NoCachedData, Weather>>
}

sealed interface DataError {
    sealed interface Local : DataError {
        data object NoCachedData : Local
    }
}
