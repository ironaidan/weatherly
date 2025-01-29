package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import com.aidannemeth.weatherly.feature.weather.domain.model.Temperature
import kotlinx.serialization.Serializable

@Serializable
data class TemperatureResource(
    val day: Temperature,
    val min: Temperature,
    val max: Temperature,
    val night: Temperature,
    val eve: Temperature,
    val morn: Temperature,
)
