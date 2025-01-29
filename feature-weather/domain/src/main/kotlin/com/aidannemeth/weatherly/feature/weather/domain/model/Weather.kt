package com.aidannemeth.weatherly.feature.weather.domain.model

data class Weather(
    val latitude: Latitude,
    val longitude: Longitude,
    val timezone: String,
    val timezoneOffset: Int,
    val temperature: Temperature,
)
