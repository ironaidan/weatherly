package com.aidannemeth.weatherly.core.data

import com.aidannemeth.weatherly.core.model.Weather

interface WeatherRepository {

    suspend fun getWeather(): Weather
}
