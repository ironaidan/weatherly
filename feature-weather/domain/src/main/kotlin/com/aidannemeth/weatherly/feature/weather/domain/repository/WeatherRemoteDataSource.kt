package com.aidannemeth.weatherly.feature.weather.domain.repository

import com.aidannemeth.weatherly.feature.weather.domain.model.Weather

interface WeatherRemoteDataSource {
    suspend fun getWeather(): Weather
}
