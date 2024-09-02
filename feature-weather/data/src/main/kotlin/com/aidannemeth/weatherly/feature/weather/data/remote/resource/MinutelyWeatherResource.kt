package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import kotlinx.serialization.Serializable

@Serializable
data class MinutelyWeatherResource(
    val dt: Long,
    val precipitation: Int,
)
