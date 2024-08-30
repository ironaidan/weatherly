package com.aidannemeth.weatherly.feature.weather.domain.repository

import arrow.core.Either
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.DataError.Local.NoCachedData
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun getWeather(): Either<NoCachedData, Weather>

    fun observeWeather(): Flow<Either<NoCachedData, Weather>>
}

sealed interface DataError {
    sealed interface Local : DataError {
        data object NoCachedData : Local
    }
}
