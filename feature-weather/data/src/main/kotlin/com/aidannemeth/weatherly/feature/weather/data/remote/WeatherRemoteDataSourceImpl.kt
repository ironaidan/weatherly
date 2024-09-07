package com.aidannemeth.weatherly.feature.weather.data.remote

import com.aidannemeth.weatherly.feature.weather.data.BuildConfig
import com.aidannemeth.weatherly.feature.weather.data.remote.mapper.toWeather
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.repository.WeatherRemoteDataSource
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRemoteDataSource {
    override suspend fun getWeather(): Weather =
        weatherApi.getWeather(
            latitude = 34.149956,
            longitude = -118.448891,
            apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
            units = "imperial"
        ).toWeather()
}
