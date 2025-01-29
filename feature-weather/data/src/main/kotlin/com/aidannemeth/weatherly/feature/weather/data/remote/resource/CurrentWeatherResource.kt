package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResource(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    @SerialName("temp")
    val temperature: Temperature,
    @SerialName("feels_like")
    val feelsLike: Temperature,
    val pressure: Int,
    val humidity: Int,
    @SerialName("dew_point")
    val dewPoint: Float,
    val uvi: Float,
    val clouds: Int,
    val visibility: Int,
    @SerialName("wind_speed")
    val windSpeed: Float,
    @SerialName("wind_deg")
    val windDirection: Int,
    val weather: List<WeatherResource>,
)
