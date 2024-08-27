package com.aidannemeth.weatherly.feature.weather.domain.repository

import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getLocalWeather(): Weather?

    fun observeWeather(): Flow<Weather?>
}
