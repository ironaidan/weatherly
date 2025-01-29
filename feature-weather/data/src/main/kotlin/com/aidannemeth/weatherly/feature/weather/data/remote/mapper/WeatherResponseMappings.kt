package com.aidannemeth.weatherly.feature.weather.data.remote.mapper

import com.aidannemeth.weatherly.feature.weather.data.remote.response.WeatherResponse
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather

fun WeatherResponse.toWeather() =
    Weather(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        temperature = current.temperature,
    )
