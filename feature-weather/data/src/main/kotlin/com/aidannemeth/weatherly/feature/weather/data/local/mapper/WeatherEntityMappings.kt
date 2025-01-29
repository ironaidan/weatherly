package com.aidannemeth.weatherly.feature.weather.data.local.mapper

import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import com.aidannemeth.weatherly.feature.weather.domain.model.Weather

fun WeatherEntity.toWeather() =
    Weather(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        temperature = temperature,
    )

fun Weather.toEntity() =
    WeatherEntity(
        latitude = latitude,
        longitude = longitude,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        temperature = temperature,
    )
