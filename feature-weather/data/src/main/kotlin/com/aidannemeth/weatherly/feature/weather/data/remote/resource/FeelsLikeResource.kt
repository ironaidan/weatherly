package com.aidannemeth.weatherly.feature.weather.data.remote.resource

import kotlinx.serialization.Serializable

@Serializable
data class FeelsLikeResource(
    val day: Float,
    val night: Float,
    val eve: Float,
    val morn: Float,
)
