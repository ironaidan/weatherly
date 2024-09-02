package com.aidannemeth.weatherly.feature.weather.domain.repository

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getLocalWeather(): Either<DataError.Local, Weather>

    fun observeWeather(): Flow<Either<DataError, Weather>>
}
