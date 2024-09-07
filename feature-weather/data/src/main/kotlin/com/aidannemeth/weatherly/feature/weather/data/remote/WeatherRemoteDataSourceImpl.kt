package com.aidannemeth.weatherly.feature.weather.data.remote

import arrow.core.Either
import com.aidannemeth.weatherly.feature.common.domain.model.DataError
import com.aidannemeth.weatherly.feature.weather.data.remote.mapper.toHttpError
import com.aidannemeth.weatherly.feature.weather.data.remote.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRemoteDataSource {
    override suspend fun getWeather(): Either<DataError.Remote, Weather> =
        weatherApi.getWeather(
            latitude = 34.149956,
            longitude = -118.448891,
            apiKey = "abcd",
            units = "imperial"
        )
            .map(::toWeather)
            .mapLeft(::toHttpError)
}
