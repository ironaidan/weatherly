package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyWeatherResource(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    @SerialName("moon_phase")
    val moonPhase: Float,
    val summary: String,
    @SerialName("temp")
    val temperature: TemperatureResource,
    @SerialName("feels_like")
    val feelsLike: FeelsLikeResource,
    val pressure: Int,
    val humidity: Int,
    @SerialName("dew_point")
    val dewPoint: Float,
    @SerialName("wind_speed")
    val windSpeed: Float,
    @SerialName("wind_deg")
    val windDirection: Int,
    @SerialName("wind_gust")
    val windGust: Float,
    val weather: List<WeatherResource>,
    val clouds: Int,
    val pop: Float,
//    val rain: Float, // not present
    val uvi: Float,
)
