package com.aidannemeth.weatherly.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val temperature: String,
)
