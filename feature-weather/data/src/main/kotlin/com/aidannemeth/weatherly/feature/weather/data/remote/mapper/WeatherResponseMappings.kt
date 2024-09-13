package com.aidannemeth.weatherly.feature.weather.data.remote.mapper

import com.aidannemeth.weatherly.feature.weather.data.remote.response.WeatherResponse
import com.aidannemeth.weatherly.feature.weather.domain.entity.MinutelyWeather
import com.aidannemeth.weatherly.feature.weather.domain.entity.Weather
import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

fun WeatherResponse.toWeather() =
    Weather(
        latitude = Latitude(latitude),
        longitude = Longitude(longitude),
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        temperature = Temperature(current.temperature),
//        minutely = minutely.map {
//            MinutelyWeather(
//                dt = it.dt,
//                precipitation = it.precipitation,
//            )
//        },
    )
