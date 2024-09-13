package com.aidannemeth.weatherly.feature.weather.data.local.mapper

import com.aidannemeth.weatherly.feature.weather.data.local.entity.MinutelyWeatherEntity
import com.aidannemeth.weatherly.feature.weather.data.local.entity.WeatherEntity
import com.aidannemeth.weatherly.feature.weather.domain.entity.MinutelyWeather
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import kotlin.math.min

fun WeatherEntity.toWeather() =
    Weather(
        latitude = Latitude(latitude),
        longitude = Longitude(longitude),
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        temperature = temperature,
//        minutely = minutely.map {
//            MinutelyWeather(
//                dt = it.dt,
//                precipitation = it.precipitation,
//            )
//        },
    )

fun Weather.toEntity() =
    WeatherEntity(
        latitude = latitude.value,
        longitude = longitude.value,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        temperature = temperature,
//        minutely = minutely.map {
//            MinutelyWeatherEntity(
//                dt = it.dt,
//                precipitation = it.precipitation,
//            )
//        },
    )
