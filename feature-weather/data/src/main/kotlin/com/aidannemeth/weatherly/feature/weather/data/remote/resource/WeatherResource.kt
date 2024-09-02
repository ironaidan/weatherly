package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResource(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String,
)
