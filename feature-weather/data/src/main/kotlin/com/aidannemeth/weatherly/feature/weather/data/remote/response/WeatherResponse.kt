package com.aidannemeth.weatherly.feature.weather.data.remote.response

import com.aidannemeth.weatherly.feature.weather.data.remote.resource.AlertResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.CurrentWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.DailyWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.HourlyWeatherResource
import com.aidannemeth.weatherly.feature.weather.data.remote.resource.MinutelyWeatherResource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    @SerialName("lat")
    val latitude: Double,
    @SerialName("lon")
    val longitude: Double,
    val timezone: String,
    @SerialName("timezone_offset")
    val timezoneOffset: Int,
    val current: CurrentWeatherResource,
    val minutely: List<MinutelyWeatherResource>,
    val hourly: List<HourlyWeatherResource>,
    val daily: List<DailyWeatherResource>,
    val alerts: List<AlertResource>,
)
