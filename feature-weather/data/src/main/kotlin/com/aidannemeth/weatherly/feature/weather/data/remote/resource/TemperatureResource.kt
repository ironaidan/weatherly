package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import kotlinx.serialization.Serializable

@Serializable
data class TemperatureResource(
    val day: Float,
    val min: Float,
    val max: Float,
    val night: Float,
    val eve: Float,
    val morn: Float,
)
