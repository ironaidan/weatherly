package com.aidannemeth.weatherly.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkWeather(
    @SerialName("weather.main")
    val weather: String,
    @SerialName("weather.description")
    val description: String,
    @SerialName("weather.icon")
    val icon: String,
    @SerialName("main.temp")
    val temperature: Float,
    @SerialName("main.feels_like")
    val feelsLike: Float,
    @SerialName("main.temp_min")
    val temperatureMin: Float,
    @SerialName("main.temp_max")
    val temperatureMax: Float,
)
