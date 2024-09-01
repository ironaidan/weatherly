package com.aidannemeth.weatherly.feature.weather.data.remote.response

import com.aidannemeth.weatherly.feature.weather.data.remote.response.WeatherResponse.CurrentWeather.Weather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("lat")
    val latitude: Float,
    @SerialName("lon")
    val longitude: Float,
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Int,
    val current: CurrentWeather,
    val minutely: List<MinutelyWeather>,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>,
    val alerts: List<Alert>,
) {
    @Serializable
    data class CurrentWeather(
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
        @SerialName("wind_gust")
        val windGust: Float,
        val weather: List<Weather>,
    ) {
        @Serializable
        data class Weather(
            val id: Int,
            val main: String,
            val description: String,
            val icon: String,
        )
    }

    @Serializable
    data class MinutelyWeather(
        val dt: Long,
        val precipitation: Int,
    )

    @Serializable
    data class HourlyWeather(
        val dt: Long,
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
        @SerialName("wind_gust")
        val windGust: Float,
        val weather: List<Weather>,
        val pop: Float,
    )

    @Serializable
    data class DailyWeather(
        val dt: Long,
        val sunrise: Long,
        val sunset: Long,
        val moonrise: Long,
        val moonset: Long,
        @SerialName("moon_phase")
        val moonPhase: Float,
        val summary: String,
        @SerialName("temp")
        val temperature: Temperature,
        @SerialName("feels_like")
        val feelsLike: FeelsLike,
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
        val weather: List<Weather>,
        val clouds: Int,
        val pop: Float,
        val rain: Float,
        val uvi: Float,
    ) {
        @Serializable
        data class Temperature(
            val day: Float,
            val min: Float,
            val max: Float,
            val night: Float,
            val eve: Float,
            val morn: Float,
        )

        @Serializable
        data class FeelsLike(
            val day: Float,
            val night: Float,
            val eve: Float,
            val morn: Float,
        )
    }

    @Serializable
    data class Alert(
        val senderName: String,
        val event: String,
        val start: Long,
        val end: Long,
        val description: String,
        val tags: List<String>,
    )
}
