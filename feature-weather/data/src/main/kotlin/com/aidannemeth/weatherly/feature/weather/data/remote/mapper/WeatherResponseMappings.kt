package com.aidannemeth.weatherly.feature.weather.data.remote.mapper

import com.aidannemeth.weatherly.feature.weather.data.remote.response.WeatherResponse
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

fun WeatherResponse.toWeather() =
    Weather(temp = Temperature(current.temperature))
