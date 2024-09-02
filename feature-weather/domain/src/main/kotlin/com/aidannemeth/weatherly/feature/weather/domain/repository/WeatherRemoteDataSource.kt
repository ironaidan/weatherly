package com.aidannemeth.weatherly.feature.weather.domain.repository

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather

interface WeatherRemoteDataSource {
    suspend fun getWeather(): Either<DataError.Remote, Weather>
}
