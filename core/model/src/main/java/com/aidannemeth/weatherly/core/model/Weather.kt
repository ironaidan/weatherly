package com.aidannemeth.weatherly.core.model

data class Weather(
    val weather: String,
    val description: String,
    val icon: String,
    val temperature: Float,
    val feelsLike: Float,
    val temperatureMin: Float,
    val temperatureMax: Float,
)
