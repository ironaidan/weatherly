package com.aidannemeth.weatherly.feature.weather.domain.repository

import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun observeWeather(): Flow<Weather>
}
