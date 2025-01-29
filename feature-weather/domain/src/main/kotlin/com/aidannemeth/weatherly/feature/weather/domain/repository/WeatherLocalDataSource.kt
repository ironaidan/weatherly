package com.aidannemeth.weatherly.feature.weather.domain.repository

import com.aidannemeth.weatherly.feature.weather.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    suspend fun deleteAll()

    suspend fun insertWeather(weather: Weather)

    fun observeWeather(): Flow<Weather?>
}
