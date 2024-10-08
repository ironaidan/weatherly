package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResource(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    @SerialName("temp")
    val temperature: Float,
    @SerialName("feels_like")
    val feelsLike: Float,
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
//    @SerialName("wind_gust")
//    val windGust: Float?, // not present in all responses
    val weather: List<WeatherResource>,
)
