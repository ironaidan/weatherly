package com.aidannemeth.weatherly.feature.weather.domain.repository

import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun observeWeather(): Flow<Weather>
}
