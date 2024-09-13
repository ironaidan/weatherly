package com.aidannemeth.weatherly.feature.weather.domain.entity

import com.aidannemeth.weatherly.feature.weather.domain.model.Latitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Longitude
import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature

data class Weather(
    val latitude: Latitude,
    val longitude: Longitude,
    val timezone: String,
    val timezoneOffset: Int,
    val temperature: Temperature,
//    val minutely: List<MinutelyWeather>,
)
