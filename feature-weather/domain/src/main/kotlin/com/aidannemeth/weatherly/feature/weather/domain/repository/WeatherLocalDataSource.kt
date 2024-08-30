package com.aidannemeth.weatherly.feature.weather.domain.repository

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError.Local.NoCachedData
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun getWeather(): Either<NoCachedData, Weather>

    fun observeWeather(): Flow<Either<NoCachedData, Weather>>
}
